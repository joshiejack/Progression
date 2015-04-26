package joshie.progression.player;

import joshie.progression.Progression;

public abstract class PlayerDataCommon {
	protected DataStats abilities = new DataStats();
	protected CriteriaMappings mappings = new CriteriaMappings();
	
	public CriteriaMappings getMappings() {
		return mappings;
	}
	
	public DataStats getAbilities() {
		return abilities;
	}
	
	protected void markDirty() {
		Progression.data.markDirty();
	}
}
