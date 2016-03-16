package joshie.progression.criteria.triggers;

import java.util.UUID;

import joshie.progression.api.ITriggerData;
import joshie.progression.criteria.triggers.data.DataCount;
import net.minecraft.item.ItemStack;

public abstract class TriggerBaseCounter extends TriggerBase {
    public int amount = 1;

    public TriggerBaseCounter(String name, int color) {
        super(name, color, "count");
    }

    public TriggerBaseCounter(String name, int color, String data) {
        super(name, color, data);
    }
    
    public TriggerBaseCounter(ItemStack stack, String name, int color) {
        super(stack, name, color, "count");
    }

    public TriggerBaseCounter(ItemStack stack, String name, int color, String data) {
        super(stack, name, color, data);
    }

    @Override
    public boolean isCompleted(ITriggerData iTriggerData) {
        return ((DataCount) iTriggerData).count >= amount;
    }

    @Override
    public boolean onFired(UUID uuid, ITriggerData iTriggerData, Object... data) {
        DataCount triggerData = (DataCount) iTriggerData;
        if (canIncrease(data)) {
            triggerData.count++;
        }

        return true;
    }

    protected boolean canIncrease(Object... data) {
        return false;
    }
}
