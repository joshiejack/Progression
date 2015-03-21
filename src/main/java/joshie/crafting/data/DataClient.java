package joshie.crafting.data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import joshie.crafting.api.tech.ITechnology;

public class DataClient extends DataCommon {
	public static final DataClient INSTANCE = new DataClient();

	private HashSet<ITechnology> unlocked = new HashSet();
	private HashMap<String, Integer> killings = new HashMap();

	@Override
	public boolean hasUnlocked(UUID uuid, ITechnology technology) {
		return unlocked.contains(technology);
	}
	
	@Override
	public boolean hasKilled(UUID uuid, String entity, int count) {
		return killings.get(entity) >= count;
	}

	public void setUnlocked(ITechnology[] techs) {
		for (ITechnology tech : techs) {
			unlocked.add(tech);
		}
	}

	public void setKillings(String entity, int count) {
		killings.put(entity, count);
	}
}
