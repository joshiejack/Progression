package joshie.crafting.api;


public interface IPlayerDataServer extends IPlayerData {
	/** Adds a speed boost to this player, Syncs the new value
	 *  with the client
	 * @param 		the speed boost  */
	public void addSpeed(float speed);

	/** Adds fall damage prevention **/
	public void addFallDamagePrevention(int maxAbsorbed);

	/** Adds research points to a player **/
    public void addResearchPoints(int amount);
}
