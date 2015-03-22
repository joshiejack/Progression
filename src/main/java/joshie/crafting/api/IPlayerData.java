package joshie.crafting.api;

import java.util.Set;
import java.util.UUID;

/** All data pertaining to this player,
 *  includes sets of everything researched, and every
 *  condition that has been met, as well additional bonus data **/
public interface IPlayerData {
	/** Gets the UUID **/
	public UUID getUUID();
	
	/** Returns a set of all the research that has been unlocked **/
	public Set<IResearch> getUnlockedResearch();
	
	/** Returns a set of all the completed conditions **/
	public Set<ICondition> getCompletedConditions();
	
	/** Marks a condition as completed
	 *  Syncs this information to the client **/
	public void markCompleted(ICondition... conditions);

	/** Unlocks the researches for the player 
	 * @return **/
	public boolean unlock(IResearch... researches);

	/** Adds a kill to the kill counter **/
	public void addKill(String entity);
	
	/** returns the amount of this entity that has been killed **/
	public int getKillCount(String entity);

	/** Returns the players speed **/
	public float getSpeed();
}
