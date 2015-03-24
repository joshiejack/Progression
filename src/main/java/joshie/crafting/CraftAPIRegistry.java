package joshie.crafting;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import joshie.crafting.api.CraftingAPI;
import joshie.crafting.api.ICraftingMappings;
import joshie.crafting.api.ICriteria;
import joshie.crafting.api.IRegistry;
import joshie.crafting.api.IReward;
import joshie.crafting.api.IRewardType;
import joshie.crafting.api.ITrigger;
import joshie.crafting.api.ITriggerType;
import joshie.crafting.player.PlayerDataServer;
import joshie.crafting.trigger.TriggerResearch;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.JsonObject;

public class CraftAPIRegistry implements IRegistry {
	//This is the registry for trigger type and reward type creation
	private final HashMap<String, ITriggerType> triggerTypes = new HashMap();
	private final HashMap<String, IRewardType> rewardTypes = new HashMap();
	
	//These four maps are registries for fetching the various types
	private final HashMap<String, ITrigger> triggers = new HashMap();
	private final HashMap<String, IReward> rewards = new HashMap();
	private final HashMap<String, ICriteria> conditions = new HashMap();

	@Override
	public Collection<ICriteria> getCriteria() {
		return conditions.values();
	}
	
	@Override //Fired Server Side only
	public boolean fireTrigger(UUID uuid, String string, Object... data) {
		return CraftingAPI.players.getServerPlayer(uuid).getMappings().fireAllTriggers(string, data);
	}
	
	@Override
	public ITriggerType registerTriggerType(ITriggerType type) {
		triggerTypes.put(type.getTypeName(), type);
		return type;
	}
	
	@Override
	public IRewardType registerRewardType(IRewardType reward) {
		rewardTypes.put(reward.getTypeName(), reward);
		return reward;
	}

	@Override
	public ICriteria newCondition(String name) {
		ICriteria condition = new CraftingCriteria().setUniqueName(name);
		conditions.put(name, condition);
		return condition;
	}
	
	@Override
	public ITrigger getTrigger(String name, String unique, JsonObject data) {
		ITrigger trigger = triggers.get(unique);
		if (trigger == null && name != null && data != null) {
			trigger = (ITrigger) triggerTypes.get(name).deserialize(data).setUniqueName(unique);
			triggers.put(unique, trigger);
		}
		
		return trigger;
	}
	
	@Override
	public IReward getReward(String name, String unique, JsonObject data) {
		IReward reward = rewards.get(unique);
		if (reward == null && name != null && data != null) {
			reward = (IReward) rewardTypes.get(name).deserialize(data).setUniqueName(unique);
			rewards.put(unique, reward);
		}
		
		return reward;
	}

	@Override
	public ICriteria getConditionFromName(String name) {
		return conditions.get(name);
	}
	
	private List technologies;

	@Override
	public List<ITrigger> getTechnology() {
		if (technologies != null) return technologies;
		else {
			technologies = new ArrayList();
			for (String name: triggers.keySet()) {
				ITrigger research = triggers.get(name);
				if (research instanceof TriggerResearch) {
					technologies.add(research);
				}
			}
			
			return technologies;
		}
	}
	
	protected Multimap<ITrigger, ICriteria> triggerToCriteria = HashMultimap.create(); //A list of triggers to the criteria that it is relevant to
	protected Multimap<ICriteria, ICriteria> criteriaToUnlocks = HashMultimap.create(); //A list of the critera completing this one unlocks

	@Override
	public Multimap<ITrigger, ICriteria> getTriggerToCriteria() {
		return triggerToCriteria;
	}

	@Override
	public Collection<ICriteria> getCriteriaUnlocks(ICriteria criteria) {
		return criteriaToUnlocks.get(criteria);
	}

	/** Returns true if any of the passed in list, is completed **/
	private boolean containsAny(List<ICriteria> list, Set<ICriteria> completedCriteria) {
		for (ICriteria criteria: completedCriteria) {
			if (completedCriteria.contains(criteria)) return true;
		}
		
		return false;
	}
	
	@Override
	public void serverRemap() {
		Collection<ICriteria> allCriteria = conditions.values();
		//Now that we have all of the criteria that been fulfilled, we need to pass through
		Collection<PlayerDataServer> data = CraftingMod.data.getPlayerData();
		for (PlayerDataServer player: data) {
			player.getMappings().remap();
			
			ICraftingMappings mappings = player.getMappings();
			for (ICriteria criteria: allCriteria) {
				//We do not give a damn about whether this is available or not
				//The unlocking of criteria should happen no matter what
				List<ICriteria> requirements = criteria.getRequirements();
				for (ICriteria require: requirements) {
					criteriaToUnlocks.put(require, criteria);
				}
				
				//Map all triggers to the criteria
				for (ITrigger trigger: criteria.getTriggers()) {
					triggerToCriteria.put(trigger, criteria);
				}
			}
		}
	}
}
