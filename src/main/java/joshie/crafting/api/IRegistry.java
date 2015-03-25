package joshie.crafting.api;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;

import com.google.common.collect.Multimap;
import com.google.gson.JsonObject;

/** The registry is where you register new types of rewards, triggers and
 *  conditions, and the criteria. All triggers and rewards should be registered before any criteria, and conditions before triggers */
public interface IRegistry {
	/** Fires all triggers, of the type specified, Triggers should only be fired
	 *  On the server side.
	 *  @return		returns true if just one of the triggers suceeded **/
	public boolean fireTrigger(UUID uuid, String trigger, Object... data);
	
	/** Convenience method **/
	public boolean fireTrigger(EntityPlayer player, String trigger, Object... data);
	
	/** Returns a criteria based on the name **/
	public ICriteria getCriteriaFromName(String name);

	/** Creates a new condition **/
	public ICondition getCondition(String type, String unique, JsonObject data);
	
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

	/** Returns a new criteria **/
	public ICriteria newCriteria(String string);
	
	public void removeCondition(String unique);
	public void removeTrigger(String unique);
	public void removeReward(String unique);
	public void removeCriteria(String unique);
	
	/** Register a condition with the registry **/
	public IConditionType registerConditionType(IConditionType reward);
	
	/** Register a trigger with the registry **/
	public ITriggerType registerTriggerType(ITriggerType trigger);
	
	/** Register a reward with the registry **/
	public IRewardType registerRewardType(IRewardType reward);

	/** Returns a list of all research that is added **/
	public List<ITrigger> getTechnology();
	
	public Multimap<ITrigger, ICriteria> getTriggerToCriteria();

	public Collection<ICriteria> getCriteriaUnlocks(ICriteria criteria);
	
	public void serverRemap();
	
	public void reloadJson();
	
	public void resetData();
	
	public void resyncPlayers();

	public Collection<ICriteria> getCriteria();

	public void loadMineTweaker3();
	
	public Collection<ITriggerType> getTriggerTypes();

	public Collection<IRewardType> getRewardTypes();
}
