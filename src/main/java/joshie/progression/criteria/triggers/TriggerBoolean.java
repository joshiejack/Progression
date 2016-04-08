package joshie.progression.criteria.triggers;

import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.criteria.ITrigger;
import joshie.progression.api.criteria.ProgressionRule;
import joshie.progression.api.special.DisplayMode;
import joshie.progression.api.special.ICustomDescription;
import joshie.progression.api.special.ICustomWidth;

import java.util.UUID;

@ProgressionRule(name="boolean", color=0xFF26C9FF, meta="onReceivedBoolean")
public class TriggerBoolean extends TriggerBaseBoolean implements ICustomDescription, ICustomWidth {
    public String variable = "default";
    public boolean isTrue = true;
    public String description = "Done some arbitrary thing you've tested for";
    public int displayWidth = 75;

    @Override
    public ITrigger copy() {
        TriggerBoolean trigger = new TriggerBoolean();
        trigger.description = description;
        trigger.displayWidth = displayWidth;
        trigger.variable = variable;
        trigger.isTrue = isTrue;
        return copyBoolean(trigger);
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public int getWidth(DisplayMode mode) {
        return mode == DisplayMode.EDIT ? 100 : displayWidth;
    }

    @Override
    public boolean onFired(UUID uuid, Object... data) {
        boolean check = ProgressionAPI.player.getBoolean(uuid, variable, false);
        if (check == isTrue) {
            markTrue();
        }

        return true;
    }
}
