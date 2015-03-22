package joshie.crafting.json;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashSet;
import java.util.Set;

import joshie.crafting.api.CraftingAPI;
import joshie.crafting.api.ICondition;
import joshie.crafting.api.IReward;
import joshie.crafting.api.ITrigger;
import joshie.crafting.api.crafting.CraftingType;
import joshie.crafting.helpers.StackHelper;
import joshie.crafting.lib.CraftingInfo;
import joshie.crafting.lib.Exceptions.ConditionNotFoundException;
import net.minecraft.item.ItemStack;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
		Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
		IJsonLoader triggers = getJson(gson, Triggers.class);
		IJsonLoader rewards = getJson(gson, Rewards.class);
		IJsonLoader conditions = getJson(gson, Conditions.class);
		IJsonLoader crafting = getJson(gson, Crafting.class);
		
		/** Step 1 we must go through and register all the triggers **/
		for (DataGeneric trigger: (HashSet<DataGeneric>) triggers.getSet()) {
			CraftingAPI.registry.getTrigger(trigger.type, trigger.name, trigger.data);
		}
		
		/** Step 2, we go through and register all the rewards **/
		for (DataGeneric reward: (HashSet<DataGeneric>) rewards.getSet()) {
			CraftingAPI.registry.getReward(reward.type, reward.name, reward.data);
		}
		
		/** Step 3, we create add all instances of conditions to the registry **/
		for (DataCondition condition: (HashSet<DataCondition>) conditions.getSet()) {
			CraftingAPI.registry.newCondition(condition.name);
		}
		
		/** Step 4, now that we have created all the conditions we can add the extra data for them **/
		for (DataCondition condition: (HashSet<DataCondition>) conditions.getSet()) {
			ICondition theCondition = CraftingAPI.registry.getConditionFromName(condition.name);
			if (theCondition == null) {
				throw new ConditionNotFoundException(condition.name);
			}
			
			ITrigger[] theTriggers = new ITrigger[condition.triggers.length];
			IReward[] theRewards = new IReward[condition.rewards.length];
			ICondition[] thePrereqs = new ICondition[condition.prereqs.length];
			ICondition[] theConflicts = new ICondition[condition.conflicts.length];
			for (int i = 0; i < theTriggers.length; i++)
				theTriggers[i] = CraftingAPI.registry.getTrigger(null, condition.triggers[i], null);
			for (int i = 0; i < theRewards.length; i++)
				theRewards[i] = CraftingAPI.registry.getReward(null, condition.rewards[i], null);
			for (int i = 0; i < thePrereqs.length; i++)
				thePrereqs[i] = CraftingAPI.registry.getConditionFromName(condition.prereqs[i]);
			for (int i = 0; i < theConflicts.length; i++)
				theConflicts[i] = CraftingAPI.registry.getConditionFromName(condition.conflicts[i]);
			theCondition.addTriggers(theTriggers).addRewards(theRewards).addPrereqs(thePrereqs).addConflicts(theConflicts);
		}
		
		/** Step 5, mark items however they should be marked **/
		for (DataCrafting craft: (HashSet<DataCrafting>) crafting.getSet()) {
			CraftingType type = getCraftingTypeFromName(craft.type);
			ItemStack stack = StackHelper.getStackFromString(craft.item);
			boolean matchDamage = craft.damage;
			boolean matchNBT = craft.nbt;
			CraftingAPI.crafting.addCondition(type, stack, matchDamage, matchNBT, craft.condition);
		}
		
		/** We are finished **/
		//Wipe out everything we don't need from memory
		dir = null;
		gson = null;
		triggers = null;
		rewards = null;
		conditions = null;
		crafting = null;
	}
	
	/** Set up the default triggers **/
	public static class Triggers extends BaseLoader {
		@Override
		public IJsonLoader setDefaults() {
			data.add(new DataGeneric("research", "IRON", "Iron Heights"));
			return this;
		}
	}
	
	/** Set up the default rewards **/
	public static class Rewards extends BaseLoader {
		@Override
		public IJsonLoader setDefaults() {
			data.add(new DataGeneric("speed", "SPEED", "0.75"));
			return this;
		}
	}
	
	/** Set up the default conditions **/
	public static class Conditions extends BaseLoader {
		@Override
		public IJsonLoader setDefaults() {
			data.add(new DataCondition("NamedCondition", new String[] { "IRON" }, new String[] { "SPEED" }, new String[] {}, new String[] {}));
			return this;
		}
	}
	
	/** Set up the default crafting **/
	public static class Crafting extends BaseLoader {
		@Override
		public IJsonLoader setDefaults() {
			data.add(new DataCrafting("CRAFTING", "minecraft:iron_block", true, false, "NamedCondition"));
			return this;
		}
	}
		
	/** The base class **/
	public static class BaseLoader implements IJsonLoader {
		protected Set data = new HashSet();
		
		@Override
		public Set getSet() {
			return data;
		}
		
		@Override
		public IJsonLoader setDefaults() {
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
