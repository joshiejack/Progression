package joshie.crafting.data;

import java.util.UUID;

import joshie.crafting.api.tech.ITechnology;

public abstract class DataCommon {
	public abstract boolean hasUnlocked(UUID uuid, ITechnology technology);
	public boolean unlockResearch(UUID uuid, ITechnology tech) { return false; }
	public boolean hasKilled(UUID uuid, String entity, int count) { return false; }
}
