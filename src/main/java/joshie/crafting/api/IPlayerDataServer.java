package joshie.crafting.api;

import java.util.Set;

public interface IPlayerDataServer extends IPlayerData {
	/** Adds a speed boost to this player, Syncs the new value
	 *  with the client
	 * @param 		the speed boost  */
	public void addSpeed(float speed);
}
