package joshie.crafting.api;

import net.minecraft.nbt.NBTTagCompound;


/** Triggers, are a type of 'condition' that needs to be met
 *  E.g. Researching a Technology, Killing x Number of Mobs.
 *  They are the individual implementation of the trigger.
 *  Triggers will only fire the condition check, on conditions
 *  that have the trigger types added */
public interface ITrigger extends IHasUniqueName, ITriggerType {		
	/** Whether this trigger has been satisfied yet 
	 * @param 		additional data stored by the trigger itself **/
	public boolean isCompleted(Object[] objects);

	/** Called when this trigger is fired, SERVERSIDE 
	 *  @param		uuid The UUID of the player, this trigger is being fired for
	 *  @param		additional data, that this trigger has stored
	 * 	@param		additional data, passed by the event itself, e.g. "Pig", for onKill 
	 * @return **/
	public Object[] onFired(Object[] existing, Object... data);
}
