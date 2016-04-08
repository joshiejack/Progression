package joshie.progression.criteria.conditions;

import joshie.progression.api.IPlayerTeam;
import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.criteria.ProgressionRule;
import joshie.progression.api.special.DisplayMode;
import joshie.progression.api.special.ICustomDescription;
import joshie.progression.api.special.ICustomWidth;

@ProgressionRule(name="boolean", color=0xFF00FFBF, meta="ifHasBoolean")
public class ConditionBoolean extends ConditionBase implements ICustomDescription, ICustomWidth {
    public String variable = "default";
    public String description = "Has done something.";
    public int displayWidth = 85;

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public int getWidth(DisplayMode mode) {
        return mode == DisplayMode.DISPLAY ? displayWidth : 100;
    }

    @Override
    public boolean isSatisfied(IPlayerTeam team) {
        return ProgressionAPI.player.getBoolean(team.getOwner(), variable, false) == !getProvider().isInverted();
    }
}
