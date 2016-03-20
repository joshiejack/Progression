package joshie.progression.player;

import joshie.progression.Progression;

public abstract class PlayerDataCommon {
	protected DataStats abilities = new DataStats();
	protected CriteriaMappings mappings = new CriteriaMappings();
	protected PlayerTeam team; //To be set on connect
	
	public CriteriaMappings getMappings() {
		return mappings;
	}
	
	public DataStats getAbilities() {
		return abilities;
	}
	
	protected void markDirty() {
		Progression.data.markDirty();
	}
	
	public PlayerTeam getTeam() {
	    return team;
	}
	
    public void setTeam(PlayerTeam team) {
        this.team = team;
    }
}
