package joshie.crafting;

import java.util.ArrayList;
import java.util.List;

import joshie.crafting.api.ICriteria;
import joshie.crafting.api.IReward;
import joshie.crafting.api.ITrigger;
import joshie.crafting.minetweaker.Criteria;
import minetweaker.MineTweakerAPI;
import scala.actors.threadpool.Arrays;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.craftcontrol.Criteria")
public class CraftingCriteria implements ICriteria {
	private List<ITrigger> triggers = new ArrayList();
	private List<IReward> rewards = new ArrayList();
	private List<ICriteria> prereqs = new ArrayList();
	private List<ICriteria> conflicts = new ArrayList();
	private int isRepeatable;
	
	private String name;
	
	@ZenMethod
	public void add(String unique, String[] triggers, @Optional String[] rewards, @Optional String[] prereqs, @Optional String[] conflicts, @Optional boolean isRepeatable) {	
		MineTweakerAPI.apply(new Criteria(unique, triggers, rewards, prereqs, conflicts, isRepeatable));
	}
	
	@Override
	public String getUniqueName() {
		return name;
	}

	@Override
	public ICriteria setUniqueName(String unique) {
		this.name = unique;
		return this;
	}
	
	@Override
	public ICriteria addTriggers(ITrigger... triggers) {
		this.triggers.addAll(Arrays.asList((ITrigger[])triggers));		
		return this;
	}
	
	@Override
	public ICriteria addRewards(IReward... rewards) {
		this.rewards.addAll(Arrays.asList((IReward[])rewards));
		for (IReward reward: rewards) {
			reward.onAdded(this);
		}
		
		return this;
	}
	
	@Override
	public ICriteria addRequirements(ICriteria... prereqs) {
		this.prereqs.addAll(Arrays.asList((ICriteria[])prereqs));
		return this;
	}

	@Override
	public ICriteria addConflicts(ICriteria... conflicts) {
		this.conflicts.addAll(Arrays.asList((ICriteria[])conflicts));
		return this;
	}
	
	@Override
	public ICriteria setRepeatAmount(int amount) {
		this.isRepeatable = amount;
		return this;
	}

	@Override
	public List<ITrigger> getTriggers() {
		return triggers;
	}

	@Override
	public List<IReward> getRewards() {
		return rewards;
	}

	@Override
	public List<ICriteria> getRequirements() {
		return prereqs;
	}

	@Override
	public List<ICriteria> getConflicts() {
		return conflicts;
	}

	@Override
	public int getRepeatAmount() {
		return isRepeatable;
	}
}
