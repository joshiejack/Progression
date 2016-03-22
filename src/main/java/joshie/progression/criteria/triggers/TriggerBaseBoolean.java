package joshie.progression.criteria.triggers;

import java.util.UUID;

import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.criteria.IProgressionTriggerData;
import net.minecraft.item.ItemStack;

public abstract class TriggerBaseBoolean extends TriggerBase {
    public TriggerBaseBoolean(ItemStack stack, String name, int color) {
        super(stack, name, color, "boolean");
    }

    @Override
    public boolean isCompleted(IProgressionTriggerData iTriggerData) {
        return ProgressionAPI.data.getBooleanData(iTriggerData);
    }

    @Override
    public boolean onFired(UUID uuid, IProgressionTriggerData iTriggerData, Object... data) {
        ProgressionAPI.data.setBooleanData(iTriggerData, isTrue(data));
        return true;
    }

    protected boolean isTrue(Object... data) {
        return false;
    }

    protected void markTrue(IProgressionTriggerData iTriggerData) {
        ProgressionAPI.data.setBooleanData(iTriggerData, true);
    }
}