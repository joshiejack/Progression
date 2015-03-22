package joshie.crafting.api;

public interface IPlayerDataServer extends IPlayerData {
	/** Adds a speed boost to this player, Syncs the new value
	 *  with the client
	 * @param 		the speed boost  */
	public void addSpeed(float speed);
}
