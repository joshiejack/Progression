package joshie.progression.player;

import joshie.progression.Progression;
import joshie.progression.player.data.AbilityStats;
import joshie.progression.player.data.CustomStats;
import joshie.progression.player.data.Points;

public abstract class PlayerDataCommon {
    protected AbilityStats abilities = new AbilityStats();
    protected CriteriaMappings mappings = new CriteriaMappings();
    protected CustomStats custom = new CustomStats();
    protected PlayerTeam team; //To be set on connect
    protected Points points = new Points();

    public CriteriaMappings getMappings() {
        return mappings;
    }

    public AbilityStats getAbilities() {
        return abilities;
    }

    public CustomStats getCustomStats() {
        return custom;
    }
    
    public Points getPoints() {
        return points;
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
