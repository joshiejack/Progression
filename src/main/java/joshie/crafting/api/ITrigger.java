package joshie.crafting.api;

import java.util.List;
import java.util.UUID;


/** Triggers, are a type of 'condition' that needs to be met
 *  E.g. Researching a Technology, Killing x Number of Mobs.
 *  They are the individual implementation of the trigger.
 *  Triggers will only fire the condition check, on conditions
 *  that have the trigger types added */
public interface ITrigger extends IHasUniqueName, ITriggerType {		
	/** Whether this trigger has been satisfied yet 
	 * @param 		additional data stored by the trigger itself **/
	public boolean isCompleted(ITriggerData triggerData);

	/** Called when this trigger is fired, SERVERSIDE 
	 * @param uuid 
	 *  @param		uuid The UUID of the player, this trigger is being fired for
	 *  @param		additional data, that this trigger has stored
	 * 	@param		additional data, passed by the event itself, e.g. "Pig", for onKill 
	 * @return **/
	public void onFired(UUID uuid, ITriggerData triggerData, Object... data);

	/** Returns a list of all the conditions this trigger needs satisfied, before it can be fired **/
	public List<ICondition> getConditions();

	/** Adds conditions to this trigger **/
	public ITrigger setConditions(ICondition[] conditions);

	/** Creates a new instance of the data for this trigger **/
	public ITriggerData newData();
}
