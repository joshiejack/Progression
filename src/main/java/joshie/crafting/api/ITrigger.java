package joshie.crafting.api;

import java.util.UUID;

/** Triggers, are a type of 'condition' that needs to be met
 *  E.g. Researching a Technology, Killing x Number of Mobs.
 *  They are the individual implementation of the trigger.
 *  Triggers will only fire the condition check, on conditions
 *  that have the trigger types added */
public interface ITrigger extends IHasUniqueName {	
	/** Create a new instanceof this trigger
	 *  Based on the data passed in **/
	public ITrigger newInstance(String data);

	/** Whether this trigger has been satisfied yet **/
	public boolean isSatisfied(UUID uuid);
}
