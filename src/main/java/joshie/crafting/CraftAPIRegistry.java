package joshie.crafting;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

import joshie.crafting.api.ICondition;
import joshie.crafting.api.IRegistry;
import joshie.crafting.api.IResearch;
import joshie.crafting.api.IReward;
import joshie.crafting.api.ITrigger;

import org.apache.logging.log4j.Level;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public class CraftAPIRegistry implements IRegistry {
	//These four maps are registries for fetching the various types
	private final HashMap<String, ITrigger> triggers = new HashMap();
	private final HashMap<String, IReward> rewards = new HashMap();
	private final HashMap<String, IResearch> researches = new HashMap();
	private final HashMap<String, ICondition> conditions = new HashMap();
	
	//This is map of trigger 'name' > conditions
	private final Multimap<String, ICondition> stringConditionMap = HashMultimap.create();
	
	@Override //Fired Server Side only
	public boolean fireTrigger(UUID uuid, String string) {
		boolean ret = false;
		Set<ICondition> conditions = (Set<ICondition>) stringConditionMap.get(string);
		for (ICondition condition: conditions) {
			if (condition.checkAndReward(uuid)) {
				ret = true;
			}
		}
		
		return ret;
	}
	
	@Override
	public ITrigger getTrigger(String name, String unique, String data) {
		ITrigger trigger = triggers.get(unique);
		if (trigger == null && name != null && data != null) {
			trigger = triggers.get(name).newInstance(data);
			triggers.put(unique, trigger);
		}
		
		return trigger;
	}
	
	@Override
	public IReward getReward(String name, String unique, String data) {
		IReward reward = rewards.get(unique);
		if (reward == null && name != null && data != null) {
			reward = rewards.get(name).newInstance(data);
			rewards.put(unique, reward);
		}
		
		return reward;
	}

	@Override
	public ICondition newCondition(String name) {
		CraftingCondition condition = new CraftingCondition(name);
		conditions.put(condition.getName(), condition);
		return condition;
	}

	@Override
	public void mapTrigger(String name, ICondition condition) {
		stringConditionMap.put(name, condition);
	}
	
	@Override
	public IResearch getResearchFromName(String name) {
		return researches.get(name);
	}

	@Override
	public ICondition getConditionFromName(String name) {
		return conditions.get(name);
	}

	@Override
	public Collection<IResearch> getResearch() {
		return researches.values();
	}
			
	@Override
	public ITrigger registerTriggerType(ITrigger trigger) {
		triggers.put(trigger.getName(), trigger);
		return trigger;
	}

	@Override
	public IReward registerRewardType(IReward reward) {
		rewards.put(reward.getName(), reward);
		return reward;
	}
	
	@Override
	public IResearch registerResearch(IResearch research) {
		researches.put(research.getName(), research);
		CraftingMod.logger.log(Level.INFO, "Registered the research: " + research.getName());
		return research;
	}
}
