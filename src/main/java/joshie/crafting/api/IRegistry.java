package joshie.crafting.api;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import com.google.common.collect.Multimap;
import com.google.gson.JsonObject;

/** The registry is where you register new types of rewards, triggers and
 *  conditions. All triggers and rewards should be registered before any conditions */
public interface IRegistry {
	/** Fires all triggers, of the type specified, Triggers should only be fired
	 *  On the server side.
	 *  @return		returns true if just one of the triggers suceeded **/
	public boolean fireTrigger(UUID uuid, String trigger, Object... data);
	
	/** Returns a condition based on the name **/
	public ICriteria getConditionFromName(String name);
	
	/** Returns a trigger with the settings, type and data can be null
	 *  If you want to just pull from the unique name, but you'd have to
	 *  make sure to call this first.
	 *  @param		the trigger type
	 *  @param		the data for the trigger **/
	public ITrigger getTrigger(String type, String unique, JsonObject data);
	
	/** Returns a new reward
	 *  @param		the reward type
	 *  @param		the data for the trigger **/
	public IReward getReward(String type, String unique, JsonObject data);

	/** Returns a new condition **/
	public ICriteria newCondition(String string);
	
	/** Register a trigger with the registry **/
	public ITriggerType registerTriggerType(ITriggerType trigger);
	
	/** Register a reward with the registry **/
	public IRewardType registerRewardType(IRewardType reward);

	/** Returns a list of all research that is added **/
	public List<ITrigger> getTechnology();
	
	public Multimap<ITrigger, ICriteria> getTriggerToCriteria();

	public Collection<ICriteria> getCriteriaUnlocks(ICriteria criteria);
	
	public void serverRemap();

	public Collection<ICriteria> getCriteria();
}
