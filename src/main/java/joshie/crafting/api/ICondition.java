package joshie.crafting.api;

import java.util.List;
import java.util.UUID;

/** Conditions are the classes that are created by the users
 *  In their config files. Conditions can have unlimited triggers
 *  as well as unlimited rewards. The conditions are checked, everytime
 *  that any trigger is fired */
public interface ICondition extends IHasUniqueName {
	/** Called by a trigger to update the state of a condition
	 *  If all triggers were met, then the condition should reward
	 *  Check and Reward is only ever called serverside
	 *  @param 		the uuid of the player, that we are checking
	 *  @return		returns true if the player was rewarded **/
	public boolean checkAndReward(UUID uuid);

	/** Returns all the triggers that this condition needs to be met **/
	public List<ITrigger> getTriggers();

	/** Adds triggers to this condition **/
	public ICondition addTriggers(ITrigger... triggers);

	/** Adds rewards to this condition **/
	public ICondition addRewards(IReward... rewards);

	/** Adds Prereqs to this condition **/
	public ICondition addPrereqs(ICondition... prereqs);
	
	/** Adds conflicts to this condition **/
	public ICondition addConflicts(ICondition... prereqs);
}
