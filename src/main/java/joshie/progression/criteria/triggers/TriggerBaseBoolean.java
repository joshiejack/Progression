package joshie.progression.criteria.triggers;

import java.util.UUID;

import joshie.progression.api.ITriggerData;
import joshie.progression.criteria.triggers.data.DataBoolean;
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
        return ((DataBoolean) iTriggerData).completed;
    }

    @Override
    public boolean onFired(UUID uuid, ITriggerData iTriggerData, Object... data) {
        ((DataBoolean) iTriggerData).completed = isTrue(data);
        return true;
    }

    protected boolean isTrue(Object... data) {
        return false;
    }

    protected void markTrue(ITriggerData iTriggerData) {
        ((DataBoolean) iTriggerData).completed = true;
    }
}