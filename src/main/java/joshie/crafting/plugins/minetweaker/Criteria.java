package joshie.crafting.plugins.minetweaker;

import joshie.crafting.CraftAPIRegistry;
import joshie.crafting.api.CraftingAPI;
import joshie.crafting.api.ICriteria;
import joshie.crafting.api.IReward;
import joshie.crafting.api.ITrigger;
import minetweaker.IUndoableAction;

public class Criteria implements IUndoableAction {
	private String unique;
	private ITrigger[] theTriggers;
	private IReward[] theRewards;
	private ICriteria[] thePrereqs;
	private ICriteria[] theConflicts;
	private boolean isRepeatable;
	
	public Criteria(String unique, String[] triggers, String[] rewards, String[] prereqs, String[] conflicts, boolean isRepeatable) {
		this.unique = unique;
		this.isRepeatable = isRepeatable;
		
		theTriggers = triggers == null? new ITrigger[0]: new ITrigger[triggers.length];
		theRewards = triggers == null? new IReward[0]: new IReward[rewards.length];
		thePrereqs = triggers == null? new ICriteria[0]: new ICriteria[prereqs.length];
		theConflicts = triggers == null? new ICriteria[0]: new ICriteria[conflicts.length];
		for (int i = 0; i < theTriggers.length; i++)
			theTriggers[i] = CraftingAPI.registry.getTrigger(null, triggers[i], null);
		for (int i = 0; i < theRewards.length; i++)
			theRewards[i] = CraftingAPI.registry.getReward(null, rewards[i], null);
		for (int i = 0; i < thePrereqs.length; i++)
			thePrereqs[i] = CraftingAPI.registry.getCriteriaFromName(prereqs[i]);
		for (int i = 0; i < theConflicts.length; i++)
			theConflicts[i] = CraftingAPI.registry.getCriteriaFromName(conflicts[i]);
	}

	@Override
	public void apply() {
		CraftingAPI.registry.newCriteria(unique).addTriggers(theTriggers).addRewards(theRewards).addRequirements(thePrereqs).addConflicts(theConflicts);
	}

	@Override
	public boolean canUndo() {
		return true;
	}

	@Override
	public void undo() {
		CraftAPIRegistry.removeCriteria(unique);
	}

	@Override
	public String describe() {
		return "Adding the criteria with the unique name of " + unique;
	}

	@Override
	public String describeUndo() {
		return "Removing the criteria with the unique name of " + unique;
	}

	@Override
	public Object getOverrideKey() {
		return null;
	}
}
