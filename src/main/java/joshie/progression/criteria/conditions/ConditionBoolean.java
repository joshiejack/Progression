package joshie.progression.criteria.conditions;

import joshie.progression.api.IPlayerTeam;
import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.special.DisplayMode;
import joshie.progression.items.ItemCriteria;

public class ConditionBoolean extends ConditionBase {
    public String variable = "default";
    public String description = "Has done something.";
    public int displayWidth = 85;

    public ConditionBoolean() {
        super(ItemCriteria.getStackFromMeta(ItemCriteria.ItemMeta.ifHasBoolean), "boolean", 0xFF00FFBF);
    }

    @Override
    public boolean isSatisfied(IPlayerTeam team) {
        boolean check = ProgressionAPI.player.getBoolean(team.getOwner(), variable);
        if (check == !inverted) {
            return true;
        }

        return false;
    }

    @Override
    public int getWidth(DisplayMode mode) {
        return mode == DisplayMode.DISPLAY ? displayWidth : super.getWidth(mode);
    }

    @Override
    public String getConditionDescription() {
        return description;
    }
}
