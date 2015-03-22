package joshie.crafting.api;

import java.util.Collection;
import java.util.UUID;

import joshie.crafting.CraftingCondition;

/** The registry is where you register new types of rewards, triggers and
 *  conditions. All triggers and rewards should be registered before any conditions */
public interface IRegistry {
	/** Fires all triggers, of the type specified, Triggers should only be fired
	 *  On the server side.
	 *  @return		returns true if just one of the triggers suceeded **/
	public boolean fireTrigger(UUID uuid, String trigger);
	
	/** Returns a research based on the name **/
	public IResearch getResearchFromName(String name);

	/** Returns a condition based on the name **/
	public ICondition getConditionFromName(String name);
	
	/** Returns a trigger with the settings, type and data can be null
	 *  If you want to just pull from the unique name, but you'd have to
	 *  make sure to call this first.
	 *  @param		the trigger type
	 *  @param		the data for the trigger **/
	public ITrigger getTrigger(String type, String unique, String data);
	
	/** Returns a new reward
	 *  @param		the reward type
	 *  @param		the data for the trigger **/
	public IReward getReward(String type, String unique, String data);

	/** Returns a new condition **/
	public ICondition newCondition(String string);
	
	/** Register a trigger with the registry **/
	public ITrigger registerTriggerType(ITrigger trigger);
	
	/** Register a reward with the registry **/
	public IReward registerRewardType(IReward reward);

	/** Automatically called when a new research trigger is created **/
	public IResearch registerResearch(IResearch research);
	
	/** Called whenever new triggers are added to a condition **/
	public void mapTrigger(String name, ICondition craftingCondition);

	/** Returns a list of all research that is added **/
	public Collection<IResearch> getResearch();
}
