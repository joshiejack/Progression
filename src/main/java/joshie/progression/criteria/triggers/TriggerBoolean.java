package joshie.progression.criteria.triggers;

import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.criteria.ITrigger;
import joshie.progression.api.special.DisplayMode;
import joshie.progression.items.ItemCriteria;

import java.util.UUID;

public class TriggerBoolean extends TriggerBaseBoolean {
    public String variable = "default";
    public boolean isTrue = true;
    public String description = "Done some arbitrary thing you've tested for";
    public int displayWidth = 75;

    public TriggerBoolean() {
        super(ItemCriteria.getStackFromMeta(ItemCriteria.ItemMeta.onReceivedBoolean), "boolean", 0xFF26C9FF);
    }

    @Override
    public ITrigger copy() {
        TriggerBoolean trigger = new TriggerBoolean();
        trigger.description = description;
        trigger.displayWidth = displayWidth;
        trigger.variable = variable;
        trigger.isTrue = isTrue;
        return copyBase(copyBoolean(trigger));
    }

    @Override
    public boolean onFired(UUID uuid, Object... data) {
        boolean check = ProgressionAPI.player.getBoolean(uuid, variable);
        if (check == isTrue) {
            markTrue();
        }

        return true;
    }

    @Override
    public int getWidth(DisplayMode mode) {
        return mode == DisplayMode.EDIT ? super.getWidth(mode) : displayWidth;
    }

    @Override
    public String getTriggerDescription() {
        return description;
    }
}
