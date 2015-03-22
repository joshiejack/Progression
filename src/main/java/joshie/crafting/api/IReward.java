package joshie.crafting.api;

import java.util.UUID;

/** A reward is what you obtain from completing a full set
 *  of triggers, E.g. Ability to craft an item, Extra Speed */
public interface IReward extends IHasUniqueName {
	/** Called when a condition has been met. This is ONLY Called ClientSide.
	 *  The reward should be given to the player whose uuid matches. */
	public void reward(UUID uuid);
	
	/** Created a new instance based on the data passed **/
	public IReward newInstance(String data);
}
