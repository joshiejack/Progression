package joshie.progression.criteria.triggers;

import java.util.UUID;

import joshie.progression.api.ITriggerData;
import joshie.progression.api.ProgressionAPI;
import net.minecraft.item.ItemStack;

public abstract class TriggerBaseBoolean extends TriggerBase {
    public TriggerBaseBoolean(ItemStack stack, String name, int color) {
        super(stack, name, color, "boolean");
    }
    
    public TriggerBaseBoolean(String name, int color) {
        super(name, color, "boolean");
    }

    @Override
    public boolean isCompleted(ITriggerData iTriggerData) {
        return ProgressionAPI.data.getBooleanData(iTriggerData);
    }

    @Override
    public boolean onFired(UUID uuid, ITriggerData iTriggerData, Object... data) {
        ProgressionAPI.data.setBooleanData(iTriggerData, isTrue(data));
        return true;
    }

    protected boolean isTrue(Object... data) {
        return false;
    }

    protected void markTrue(ITriggerData iTriggerData) {
        ProgressionAPI.data.setBooleanData(iTriggerData, true);
    }
}