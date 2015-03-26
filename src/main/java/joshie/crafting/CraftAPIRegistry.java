package joshie.crafting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import joshie.crafting.api.CraftingAPI;
import joshie.crafting.api.ICondition;
import joshie.crafting.api.IConditionType;
import joshie.crafting.api.ICriteria;
import joshie.crafting.api.IRegistry;
import joshie.crafting.api.IResearch;
import joshie.crafting.api.IReward;
import joshie.crafting.api.IRewardType;
import joshie.crafting.api.ITrigger;
import joshie.crafting.api.ITriggerType;
import joshie.crafting.helpers.PlayerHelper;
import net.minecraft.entity.player.EntityPlayer;

import com.google.gson.JsonObject;

public class CraftAPIRegistry implements IRegistry {
	//This is the registry for trigger type and reward type creation
	public static final HashMap<String, ITriggerType> triggerTypes = new HashMap();
	public static final HashMap<String, IRewardType> rewardTypes = new HashMap();
	public static final HashMap<String, IConditionType> conditionTypes = new HashMap();
	
	//These four maps are registries for fetching the various types
	public static HashMap<String, ITrigger> triggers = new HashMap();
	public static HashMap<String, IReward> rewards = new HashMap();
	public static HashMap<String, ICriteria> criteria = new HashMap();
	public static HashMap<String, ICondition> conditions = new HashMap();

	@Override //Fired Server Side only
	public boolean fireTrigger(UUID uuid, String string, Object... data) {
		return CraftingAPI.players.getServerPlayer(uuid).getMappings().fireAllTriggers(string, data);
	}
	
	@Override //Fired Server Side only
	public boolean fireTrigger(EntityPlayer player, String string, Object... data) {
		if (!player.worldObj.isRemote) {
			return fireTrigger(PlayerHelper.getUUIDForPlayer(player), string, data);
		} else return false;
	}

	@Override
	public IConditionType registerConditionType(IConditionType type) {
		conditionTypes.put(type.getTypeName(), type);
		return type;
	}
	
	@Override
	public ITriggerType registerTriggerType(ITriggerType type) {
		triggerTypes.put(type.getTypeName(), type);
		return type;
	}
	
	@Override
	public IRewardType registerRewardType(IRewardType type) {
		rewardTypes.put(type.getTypeName(), type);
		return type;
	}

	@Override
	public ICriteria newCriteria(String name) {
		ICriteria condition = new CraftingCriteria().setUniqueName(name);
		criteria.put(name, condition);
		return condition;
	}

	@Override
	public ICondition getCondition(String name, String unique, JsonObject data) {
		ICondition condition = conditions.get(unique);
		if (condition == null && name != null && data != null) {
			boolean inverted = data.get("inverted") != null? data.get("inverted").getAsBoolean(): false;
			condition = (ICondition) conditionTypes.get(name).deserialize(data).setInversion(inverted).setUniqueName(unique);
			conditions.put(unique, condition);
		}
		
		return condition;
	}
	
	@Override
	public ITrigger getTrigger(String name, String unique, JsonObject data) {
		ITrigger trigger = triggers.get(unique);
		if (trigger == null && name != null && data != null) {
			//New trigger created
			trigger = (ITrigger) triggerTypes.get(name).deserialize(data).setUniqueName(unique);
			CraftingEventsManager.onTriggerAdded(trigger);
			triggers.put(unique, trigger);
		}
		
		return trigger;
	}
	
	@Override
	public IReward getReward(String name, String unique, JsonObject data) {
		IReward reward = rewards.get(unique);
		if (reward == null && name != null && data != null) {
			reward = (IReward) rewardTypes.get(name).deserialize(data).setUniqueName(unique);
			CraftingEventsManager.onRewardAdded(reward);
			rewards.put(unique, reward);
		}
		
		return reward;
	}

	@Override
	public ICriteria getCriteriaFromName(String name) {
		return criteria.get(name);
	}
	
	private static List<IResearch> technologies;

	@Override
	public List<IResearch> getTechnology() {
		if (technologies != null) return technologies;
		else {
			technologies = new ArrayList();
			for (String name: triggers.keySet()) {
				ITrigger research = triggers.get(name);
				if (research instanceof IResearch) {
					technologies.add((IResearch)research);
				}
			}
			
			return technologies;
		}
	}
	
	/** Convenience methods for removals **/
	public static void removeCondition(String unique) {
		conditions.remove(unique);
	}

	public static void removeTrigger(String unique) {
		ITrigger trigger = triggers.get(unique);
		CraftingEventsManager.onTriggerRemoved(trigger);
		triggers.remove(unique);
	}

	public static void removeReward(String unique) {
		IReward reward = rewards.get(unique);
		CraftingEventsManager.onRewardRemoved(reward);
		rewards.remove(unique);
	}

	public static void removeCriteria(String unique) {
		criteria.remove(unique);
	}
}
