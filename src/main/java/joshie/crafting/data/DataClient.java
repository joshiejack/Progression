package joshie.crafting.data;

import java.util.HashSet;
import java.util.UUID;

import joshie.crafting.api.tech.ITechnology;

public class DataClient extends DataCommon {
	public static final DataClient INSTANCE = new DataClient();

	private HashSet<ITechnology> unlocked = new HashSet();

	@Override
	public boolean hasUnlocked(UUID uuid, ITechnology technology) {
		return unlocked.contains(technology);
	}

	public void setUnlocked(ITechnology[] techs) {
		for (ITechnology tech : techs) {
			unlocked.add(tech);
		}
	}
}
