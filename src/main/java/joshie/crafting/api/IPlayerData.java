package joshie.crafting.api;

import java.util.Set;
import java.util.UUID;

/** All data pertaining to this player,
 *  includes sets of everything researched, and every
 *  criteria that has been met, as well additional bonus data **/
public interface IPlayerData {
	/** Gets the UUID **/
	public UUID getUUID();

	/** Returns the players speed **/
	public float getSpeed();

	public ICraftingMappings getMappings();
	
	public void resetData();
}
