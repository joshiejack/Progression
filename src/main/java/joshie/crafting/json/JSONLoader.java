package joshie.crafting.json;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashSet;
import java.util.Set;

import joshie.crafting.api.CraftingAPI;
import joshie.crafting.api.ICriteria;
import joshie.crafting.api.IReward;
import joshie.crafting.api.ITrigger;
import joshie.crafting.api.crafting.CraftingType;
import joshie.crafting.lib.CraftingInfo;
import joshie.crafting.lib.Exceptions.ConditionNotFoundException;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class JSONLoader {
	/** Load in the json **/
	private static IJsonLoader getJson(Gson gson, Class clazz) {
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
		GsonBuilder builder = new GsonBuilder().setPrettyPrinting();
		builder.registerTypeAdapter(DataTrigger.class, new AdapterTriggers());
		builder.registerTypeAdapter(DataReward.class, new AdapterRewards());
		Gson gson = builder.create();
		IJsonLoader triggers = getJson(gson, Triggers.class);
		IJsonLoader rewards = getJson(gson, Rewards.class);
		IJsonLoader criteria = getJson(gson, Criteria.class);
		
		/** Step 1 we must go through and register all the triggers **/
		for (DataTrigger trigger: (HashSet<DataTrigger>) triggers.getSet()) {
			CraftingAPI.registry.getTrigger(trigger.type, trigger.name, trigger.data);
		}
		
		/** Step 2, we go through and register all the rewards **/
		for (DataReward reward: (HashSet<DataReward>) rewards.getSet()) {
			CraftingAPI.registry.getReward(reward.type, reward.name, reward.data);
		}
		
		/** Step 3, we create add all instances of conditions to the registry **/
		for (DataCondition condition: (HashSet<DataCondition>) criteria.getSet()) {
			CraftingAPI.registry.newCondition(condition.name);
		}
		
		/** Step 4, now that we have created all the conditions we can add the extra data for them **/
		for (DataCondition condition: (HashSet<DataCondition>) criteria.getSet()) {
			ICriteria theCondition = CraftingAPI.registry.getConditionFromName(condition.name);
			if (theCondition == null) {
				throw new ConditionNotFoundException(condition.name);
			}
			
			ITrigger[] theTriggers = new ITrigger[condition.triggers.length];
			IReward[] theRewards = new IReward[condition.rewards.length];
			ICriteria[] thePrereqs = new ICriteria[condition.prereqs.length];
			ICriteria[] theConflicts = new ICriteria[condition.conflicts.length];
			for (int i = 0; i < theTriggers.length; i++)
				theTriggers[i] = CraftingAPI.registry.getTrigger(null, condition.triggers[i], null);
			for (int i = 0; i < theRewards.length; i++)
				theRewards[i] = CraftingAPI.registry.getReward(null, condition.rewards[i], null);
			for (int i = 0; i < thePrereqs.length; i++)
				thePrereqs[i] = CraftingAPI.registry.getConditionFromName(condition.prereqs[i]);
			for (int i = 0; i < theConflicts.length; i++)
				theConflicts[i] = CraftingAPI.registry.getConditionFromName(condition.conflicts[i]);
			boolean repeatable = condition.repeatable;
			theCondition.addTriggers(theTriggers).addRewards(theRewards).addRequirements(thePrereqs).addConflicts(theConflicts).setRepeatable(repeatable);
		}
		
		/** We are finished **/
		//Wipe out everything we don't need from memory
		dir = null;
		gson = null;
		triggers = null;
		rewards = null;
		criteria = null;
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
			JsonObject iron = new JsonObject();
			iron.addProperty("Research Name", "Iron Heights");
			data.add(new DataTrigger("research", "IRON", iron));
			JsonObject pig = new JsonObject();
			pig.addProperty("Entity", "Pig");
			data.add(new DataTrigger("kill", "GOLD", pig));
			JsonObject crafting = new JsonObject();
			crafting.addProperty("Item", "minecraft:diamond_block");
			data.add(new DataTrigger("crafting", "LAPIS", crafting));
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
			data.add(new DataReward("speed", "SPEED", speed));
			JsonObject iron = new JsonObject();
			iron.addProperty("Item", "minecraft:iron_block");
			data.add(new DataReward("crafting", "CRAFTINGIRON", iron));
			JsonObject gold = new JsonObject();
			gold.addProperty("Item", "minecraft:gold_block");
			data.add(new DataReward("crafting", "CRAFTINGGOLD", gold));
			JsonObject lapis = new JsonObject();
			lapis.addProperty("Item", "minecraft:lapis_block");
			data.add(new DataReward("crafting", "CRAFTINGLAPIS", lapis));
			return this;
		}
	}
	
	/** Set up the default conditions **/
	public static class Criteria implements IJsonLoader {
		private Set<DataCondition> data = new HashSet();

		@Override
		public Set getSet() {
			return data;
		}
		
		@Override
		public IJsonLoader setDefaults() {
			data.add(new DataCondition("NamedCondition", new String[] { "IRON" }, new String[] { "SPEED", "CRAFTINGIRON" }, new String[] {}, new String[] {}));
			data.add(new DataCondition("GoldenPig", new String[] { "GOLD" }, new String[] { "CRAFTINGGOLD" }, new String[] {}, new String[] {}));
			data.add(new DataCondition("EnableLapis", new String[] { "LAPIS" }, new String[] { "CRAFTINGLAPIS" }, new String[] {}, new String[] {}));
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
