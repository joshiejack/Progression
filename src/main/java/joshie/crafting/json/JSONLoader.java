package joshie.crafting.json;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import joshie.crafting.CraftAPIRegistry;
import joshie.crafting.CraftingMod;
import joshie.crafting.api.CraftingAPI;
import joshie.crafting.api.ICondition;
import joshie.crafting.api.ICriteria;
import joshie.crafting.api.IReward;
import joshie.crafting.api.ITrigger;
import joshie.crafting.api.crafting.CraftingType;
import joshie.crafting.helpers.StackHelper;
import joshie.crafting.lib.CraftingInfo;
import joshie.crafting.lib.Exceptions.ConditionNotFoundException;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class JSONLoader {
    public static Gson gson;
    static {
        GsonBuilder builder = new GsonBuilder().setPrettyPrinting();
        builder.registerTypeAdapter(DataTrigger.class, new AdapterTriggers());
        builder.registerTypeAdapter(DataReward.class, new AdapterRewards());
        builder.registerTypeAdapter(DataCondition.class, new AdapterConditions());
        gson = builder.create();
    }
    
	/** Load in the json **/
	private static Options getJson(Class clazz) {
		try {
			File file = new File("config" + File.separator + CraftingInfo.MODPATH + File.separator + clazz.getSimpleName().toLowerCase() + ".json");
			if (!file.exists()) { //If the json file doesn't exist, let make one with default values
				Writer writer = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
				writer.write(gson.toJson(clazz.newInstance()));
				writer.close();
			} 
			
			return gson.fromJson(FileUtils.readFileToString(file), clazz);
		} catch (Exception e) {} //Fail JSON Silently
		return null; //Return it whether it's null or not
	}
	
	private static IJsonLoader getJsonLoader(Class clazz) {
		IJsonLoader loader = null;
		try {
			File file = new File("config" + File.separator + CraftingInfo.MODPATH + File.separator + clazz.getSimpleName().toLowerCase() + ".json");
			if (!file.exists()) { //If the json file doesn't exist, let make one with default values
				loader = ((IJsonLoader) clazz.newInstance()).setDefaults();
				Writer writer = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
				writer.write(gson.toJson(loader));
				writer.close();
				return loader;
			} else return gson.fromJson(FileUtils.readFileToString(file), clazz);
		} catch (Exception e) {} //Fail JSON Silently
		return loader; //Return it whether it's null or not
	}
	
	private static CraftingType getCraftingTypeFromName(String name) {
		for (CraftingType type: CraftingType.values()) {
			if(name.equalsIgnoreCase(type.name())) return type;
		}
		
		return CraftingType.CRAFTING;
	}
	
	public static void loadJSON() {				
		/** Create the config directory **/
		File dir = new File("config" + File.separator + CraftingInfo.MODPATH);
		if (!dir.exists()) {
			dir.mkdir();
		}
				
		/** Grab yourself some gson **/
		CraftingMod.options = getJson(Options.class);
		IJsonLoader conditions = getJsonLoader(Conditions.class);
		IJsonLoader triggers = getJsonLoader(Triggers.class);
		IJsonLoader rewards = getJsonLoader(Rewards.class);
		IJsonLoader criterian = getJsonLoader(Criteria.class);
		
		/** Step 0, register all the conditions **/
		for (DataCondition condition: (HashSet<DataCondition>) conditions.getSet()) {
			CraftingAPI.registry.getCondition(condition.type, condition.name, condition.data);
		}
		
		/** Step 1 we must go through and register all the triggers **/
		for (DataTrigger trigger: (HashSet<DataTrigger>) triggers.getSet()) {
			ITrigger theTrigger = CraftingAPI.registry.getTrigger(trigger.type, trigger.name, trigger.data);
			ICondition[] theConditions = new ICondition[trigger.conditions.length];
			for (int i = 0; i < theConditions.length; i++)
				theConditions[i] = CraftingAPI.registry.getCondition(null, trigger.conditions[i], null);
			theTrigger.setConditions(theConditions);
		}
		
		/** Step 2, we go through and register all the rewards **/
		for (DataReward reward: (HashSet<DataReward>) rewards.getSet()) {
			CraftingAPI.registry.getReward(reward.type, reward.name, reward.data);
		}
		
		/** Step 3, we create add all instances of criteria to the registry **/
		for (DataCriteria criteria: (HashSet<DataCriteria>) criterian.getSet()) {
			CraftingAPI.registry.newCriteria(criteria.name);
		}
		
		/** Step 4, now that we have created all the criteria we can add the extra data for them **/
		for (DataCriteria criteria: (HashSet<DataCriteria>) criterian.getSet()) {
			ICriteria theCriteria = CraftingAPI.registry.getCriteriaFromName(criteria.name);
			if (theCriteria == null) {
				throw new ConditionNotFoundException(criteria.name);
			}
			
			ITrigger[] theTriggers = new ITrigger[criteria.triggers.length];
			IReward[] theRewards = new IReward[criteria.rewards.length];
			ICriteria[] thePrereqs = new ICriteria[criteria.prereqs.length];
			ICriteria[] theConflicts = new ICriteria[criteria.conflicts.length];
			for (int i = 0; i < theTriggers.length; i++)
				theTriggers[i] = CraftingAPI.registry.getTrigger(null, criteria.triggers[i], null);
			for (int i = 0; i < theRewards.length; i++)
				theRewards[i] = CraftingAPI.registry.getReward(null, criteria.rewards[i], null);
			for (int i = 0; i < thePrereqs.length; i++)
				thePrereqs[i] = CraftingAPI.registry.getCriteriaFromName(criteria.prereqs[i]);
			for (int i = 0; i < theConflicts.length; i++)
				theConflicts[i] = CraftingAPI.registry.getCriteriaFromName(criteria.conflicts[i]);
			int repeatable = criteria.repeatable;
			int x = criteria.x;
			int y = criteria.y;
			theCriteria.addTriggers(theTriggers).addRewards(theRewards).addRequirements(thePrereqs).addConflicts(theConflicts).setRepeatAmount(repeatable).setCoordinates(x, y);
		}
		
		/** We are finished **/
		//Wipe out everything we don't need from memory
		dir = null;
		triggers = null;
		rewards = null;
		criterian = null;
	}
	
	public static DataCriteria getDataForCriteria(ICriteria criteria) {
	    DataCriteria data = new DataCriteria();
	    data.x = criteria.getX();
	    data.y = criteria.getY();
	    data.repeatable = criteria.getRepeatAmount();
	    data.name = criteria.getUniqueName();
	    List<ITrigger> triggers = criteria.getTriggers();
	    List<IReward> rewards = criteria.getRewards();
	    List<ICriteria> prereqs = criteria.getRequirements();
	    List<ICriteria> conflicts = criteria.getConflicts();
	    
	    String[] theTriggers = new String[triggers.size()];
	    String[] theRewards = new String[rewards.size()];
	    String[] thePrereqs = new String[prereqs.size()];
	    String[] theConflicts = new String[conflicts.size()];
        for (int i = 0; i < theTriggers.length; i++)
            theTriggers[i] = triggers.get(i).getUniqueName();
        for (int i = 0; i < theRewards.length; i++)
            theRewards[i] = rewards.get(i).getUniqueName();
        for (int i = 0; i < thePrereqs.length; i++)
            thePrereqs[i] = prereqs.get(i).getUniqueName();
        for (int i = 0; i < theConflicts.length; i++)
            theConflicts[i] = conflicts.get(i).getUniqueName();
	    data.triggers = theTriggers;
	    data.rewards = theRewards;
	    data.prereqs = thePrereqs;
	    data.conflicts = theConflicts;
	    return data;
	}
	
	public static void saveJSON(Object toSave) {
	    File file = new File("config" + File.separator + CraftingInfo.MODPATH + File.separator + toSave.getClass().getSimpleName().toLowerCase() + ".json");
        try {
            Writer writer = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
            writer.write(gson.toJson(toSave));
            writer.close();
        } catch (Exception e) { e.printStackTrace(); }
	}
	
	public static void saveCriteria() {
        Collection<ICriteria> criteria = CraftAPIRegistry.criteria.values();
        Criteria forJSONCriteria = new Criteria();
        for (ICriteria c: criteria) {
            forJSONCriteria.data.add(getDataForCriteria(c));
        }
        
        saveJSON(forJSONCriteria);
	}
	
	public static void saveTriggers() {
	    Collection<ITrigger> trigger = CraftAPIRegistry.triggers.values();
        Triggers forJSONTriggers = new Triggers();
        for(ITrigger t: trigger) {
            DataTrigger condition = new DataTrigger();
            condition.type = t.getTypeName();
            condition.name = t.getUniqueName();
            JsonObject object = new JsonObject();
            t.serialize(object);
            condition.data = object;
            List<ICondition> conditions = t.getConditions();
            String[] theConditions = new String[conditions.size()];
            for (int i = 0; i < theConditions.length; i++)
                theConditions[i] = conditions.get(i).getUniqueName();
            condition.conditions = theConditions;
            forJSONTriggers.data.add(condition);
        }
        
        saveJSON(forJSONTriggers);
	}
	
	public static void saveRewards() {
	    Collection<IReward> reward = CraftAPIRegistry.rewards.values();
	    Rewards forJSONRewards = new Rewards();
        for(IReward r: reward) {
            DataReward condition = new DataReward();
            condition.type = r.getTypeName();
            condition.name = r.getUniqueName();
            JsonObject object = new JsonObject();
            r.serialize(object);
            condition.data = object;
            forJSONRewards.data.add(condition);
        }
        
        saveJSON(forJSONRewards);
	}
	
	public static void saveConditions() {
	    Collection<ICondition> iCondition = CraftAPIRegistry.conditions.values();
        Conditions forJSONCondition = new Conditions();
        for(ICondition c: iCondition) {
            DataCondition condition = new DataCondition();
            condition.type = c.getTypeName();
            condition.name = c.getUniqueName();
            JsonObject object = new JsonObject();
            c.serialize(object);
            condition.data = object;
            forJSONCondition.data.add(condition);
        }
        
        saveJSON(forJSONCondition);
	}
	
	public static void saveJSON() {
	    saveCriteria();
	    saveTriggers();
	    saveRewards();
	    saveConditions();
	}

    /** Setup default conditions **/
	public static class Conditions implements IJsonLoader {
		private Set<DataCondition> data = new HashSet();

		@Override
		public Set getSet() {
			return data;
		}
		
		@Override
		public IJsonLoader setDefaults() {
			JsonObject night = new JsonObject();
			night.addProperty("Night", true);
			data.add(new DataCondition("time", "TIME", night));
			return this;
		}
	}
	
	/** Set up the default triggers **/
	public static class Triggers implements IJsonLoader {
		private Set<DataTrigger> data = new HashSet();

		@Override
		public Set getSet() {
			return data;
		}
		
		@Override
		public IJsonLoader setDefaults() {
			JsonObject object = new JsonObject();
			ItemStack stack = new ItemStack(Blocks.bookshelf);			
			String serial = StackHelper.getStringFromStack(stack);
			object.addProperty("Item", serial);
			object.addProperty("Amount", 5);
			data.add(new DataTrigger("Break Block", "BREAKBOOK", object, new String[] { }));
			JsonObject iron = new JsonObject();
			iron.addProperty("Research Name", "Iron Heights");
			data.add(new DataTrigger("Research", "IRON", iron, new String[] {}));
			JsonObject pig = new JsonObject();
			pig.addProperty("Entity", "Pig");
			data.add(new DataTrigger("Kill", "GOLD", pig, new String[] { "TIME" }));
			JsonObject crafting = new JsonObject();
			crafting.addProperty("Item", "minecraft:diamond_block");
			data.add(new DataTrigger("Crafting", "LAPIS", crafting, new String[] {}));
			return this;
		}
	}
	
	/** Set up the default rewards **/
	public static class Rewards implements IJsonLoader {
		private Set<DataReward> data = new HashSet();

		@Override
		public Set getSet() {
			return data;
		}
		
		@Override
		public IJsonLoader setDefaults() {
			JsonObject speed = new JsonObject();
			speed.addProperty("Speed", 0.1F);
			data.add(new DataReward("Speed", "SPEED", speed));
			JsonObject iron = new JsonObject();
			iron.addProperty("Item", "minecraft:iron_block");
			data.add(new DataReward("Crafting", "CRAFTINGIRON", iron));
			JsonObject gold = new JsonObject();
			gold.addProperty("Item", "minecraft:gold_block");
			data.add(new DataReward("Crafting", "CRAFTINGGOLD", gold));
			JsonObject lapis = new JsonObject();
			lapis.addProperty("Item", "minecraft:lapis_block");
			data.add(new DataReward("Crafting", "CRAFTINGLAPIS", lapis));
			return this;
		}
	}
	
	/** Set up the default conditions **/
	public static class Criteria implements IJsonLoader {
		private Set<DataCriteria> data = new HashSet();

		@Override
		public Set getSet() {
			return data;
		}
		
		@Override
		public IJsonLoader setDefaults() {
			data.add(new DataCriteria("NEW CONDITION", new String[] { "BREAKBOOK" }, new String[] { "SPEED", "CRAFTINGLAPIS" }, new String[] { "GoldenPig" }, new String[] {}, 55, 0));
			data.add(new DataCriteria("NamedCondition", new String[] { "IRON" }, new String[] { "SPEED", "CRAFTINGIRON" }, new String[] {}, new String[] {}, 0, 55));
			data.add(new DataCriteria("GoldenPig", new String[] { "GOLD" }, new String[] { "CRAFTINGGOLD" }, new String[] {}, new String[] {}, 0, 0));
			data.add(new DataCriteria("EnableLapis", new String[] { "LAPIS" }, new String[] { "CRAFTINGLAPIS" }, new String[] {}, new String[] {}, 0, 110));
			return this;
		}
	}
	
	public static interface IJsonLoader {
		/** Returns the set **/
		public Set getSet();

		/** Set the default loader **/
		public IJsonLoader setDefaults();
	}
}
