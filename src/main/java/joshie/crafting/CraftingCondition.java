package joshie.crafting;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import joshie.crafting.api.CraftingAPI;
import joshie.crafting.api.ICondition;
import joshie.crafting.api.IPlayerDataServer;
import joshie.crafting.api.IReward;
import joshie.crafting.api.ITrigger;
import scala.actors.threadpool.Arrays;

public class CraftingCondition implements ICondition {
	private List<ITrigger> triggers = new ArrayList();
	private List<IReward> rewards = new ArrayList();
	private List<ICondition> prereqs = new ArrayList();
	private List<ICondition> conflicts = new ArrayList();
	
	private final String name;
	public CraftingCondition(String name) {
		this.name = name;
	}
	
	@Override
	public ICondition addTriggers(ITrigger... triggers) {
		this.triggers.addAll(Arrays.asList((ITrigger[])triggers));
		for (ITrigger trigger: triggers) {
			CraftingAPI.registry.mapTrigger(trigger.getName(), this);
		}
		
		return this;
	}
	
	@Override
	public ICondition addRewards(IReward... rewards) {
		this.rewards.addAll(Arrays.asList((IReward[])rewards));
		return this;
	}
	
	@Override
	public ICondition addPrereqs(ICondition... prereqs) {
		this.prereqs.addAll(Arrays.asList((ICondition[])prereqs));
		return this;
	}

	@Override
	public ICondition addConflicts(ICondition... conflicts) {
		this.conflicts.addAll(Arrays.asList((ICondition[])conflicts));
		return this;
	}
	
	@Override
	public boolean checkAndReward(UUID uuid) {
		//First we should check if the player has satisfied this condition
		IPlayerDataServer data = CraftingAPI.players.getServerPlayer(uuid);
		Set<ICondition> completed = data.getCompletedConditions();
		if (completed.contains(this)) return false;
		
		//Check the backlist
		for (ICondition condition: conflicts) {
			if (completed.contains(condition)) return false;
		}
		
		//Check for all prereqs
		if (!completed.containsAll(prereqs)) return false;
		for (ITrigger trigger: triggers) {
			if (!trigger.isSatisfied(uuid)) return false;
		}
		
		//Now that we know all triggers have been satified,
		for (IReward reward: rewards) {
			reward.reward(uuid);
		}
		
		//Mark this as completed
		data.markCompleted(this);
		
		return true;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public List<ITrigger> getTriggers() {
		return triggers;
	}
}
