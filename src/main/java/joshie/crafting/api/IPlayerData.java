package joshie.crafting.api;

import java.util.UUID;

import joshie.crafting.player.DataStats;

/** All data pertaining to this player,
 *  includes sets of everything researched, and every
 *  criteria that has been met, as well additional bonus data **/
public interface IPlayerData {
	/** Gets the UUID **/
	public UUID getUUID();

	public ICraftingMappings getMappings();

    public DataStats getAbilities();
}
