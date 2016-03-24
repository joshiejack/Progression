package joshie.progression.criteria.triggers;

import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.criteria.IProgressionTriggerData;
import joshie.progression.items.ItemCriteria;

import java.util.UUID;

public class TriggerBoolean extends TriggerBaseBoolean {
    public String variable = "default";
    public boolean isTrue = true;

    public TriggerBoolean() {
        super(ItemCriteria.getStackFromMeta(ItemCriteria.ItemMeta.onReceivedBoolean), "boolean", 0xFF26C9FF);
    }

    @Override
    public boolean onFired(UUID uuid, IProgressionTriggerData iTriggerData, Object... data) {
        boolean check = ProgressionAPI.player.getBoolean(uuid, variable);
        if (check == isTrue) {
            markTrue(iTriggerData);
        }

        return true;
    }
}
