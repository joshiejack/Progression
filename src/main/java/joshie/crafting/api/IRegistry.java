package joshie.crafting.api;

import java.util.List;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;

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
	
	/** Returns a tab based on the name **/
	public ITab getTabFromName(String name);

	/** Creates a new condition **/
	public ICondition newCondition(ICriteria criteria, String type, JsonObject data);
	
	/** Returns a trigger with the settings, type and data can be null
	 *  If you want to just pull from the unique name, but you'd have to
	 *  make sure to call this first.
	 *  @param      the criteria 
	 *  @param		the trigger type
	 *  @param		the data for the trigger **/
	public ITrigger newTrigger(ICriteria criteria, String type, JsonObject data);
	
	/** Creates a fresh trigger **/
    public ITrigger cloneTrigger(ICriteria criteria, ITriggerType trigger);
		
	/** Returns a new reward
	 *  @param		the reward type
	 *  @param		the data for the trigger **/
	public IReward newReward(ICriteria criteria, String type, JsonObject data);
	
	/** Creates a fresh reward **/
	public IReward cloneReward(ICriteria criteria, IRewardType reward);

	/** Creates a clone of this condition, and attaches it to the trigger **/
    public ICondition cloneCondition(ITrigger trigger, IConditionType condition);

	/** Returns a new criteria 
	 * @param tab **/
	public ICriteria newCriteria(ITab tab, String string);
	
	/** Returns a new tab **/
    public ITab newTab(String uniqueName);
	
	/** Register a condition with the registry **/
	public IConditionType registerConditionType(IConditionType reward);
	
	/** Register a trigger with the registry **/
	public ITriggerType registerTriggerType(ITriggerType trigger);
	
	/** Register a reward with the registry **/
	public IRewardType registerRewardType(IRewardType reward);

	/** Returns a list of all the research technologies **/
	public List<IResearch> getTechnology();
}
