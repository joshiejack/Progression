package joshie.crafting.player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import joshie.crafting.CraftingMod;
import joshie.crafting.api.ICondition;
import joshie.crafting.api.IPlayerData;
import joshie.crafting.api.IResearch;

public abstract class PlayerDataCommon implements IPlayerData {
	protected Set<IResearch> unlocked = new HashSet();
	protected Set<ICondition> completed = new HashSet();
	protected HashMap<String, Integer> kills = new HashMap();
	protected DataAbilities abilities = new DataAbilities();
	
	@Override
	public Set<IResearch> getUnlockedResearch() {
		return unlocked;
	}
	
	@Override
	public boolean unlock(IResearch... researchs) {
		boolean didUnlock = false;
		for (IResearch research: researchs) {
			if (unlocked.add(research)) {
				didUnlock = true;
			}
		}
		
		return didUnlock;
	}
	@Override
	public void markCompleted(ICondition... conditions) {
		for (ICondition condition: conditions) {
			completed.add(condition);
		}
	}
	
	@Override
	public float getSpeed() {
		return abilities.getSpeed();
	}

	@Override
	public Set<ICondition> getCompletedConditions() {
		return completed;
	}

	@Override
	public int getKillCount(String entity) {
		return kills.get(entity);
	}
	
	protected void markDirty() {
		CraftingMod.data.markDirty();
	}
}
