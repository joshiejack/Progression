package joshie.progression.criteria.triggers;

import joshie.progression.api.ITriggerData;
import joshie.progression.criteria.triggers.data.DataCount;
import net.minecraft.item.ItemStack;

public abstract class TriggerBaseCounterVaries extends TriggerBaseCounter {
    public boolean greaterThan = true;
    public boolean isEqualTo = true;
    public boolean lesserThan = false;

    public TriggerBaseCounterVaries(String name, int color) {
        super(name, color, "count");
    }

    public TriggerBaseCounterVaries(String name, int color, String data) {
        super(name, color, data);
    }
    
    public TriggerBaseCounterVaries(ItemStack stack, String name, int color) {
        super(stack, name, color, "count");
    }

    public TriggerBaseCounterVaries(ItemStack stack, String name, int color, String data) {
        super(stack, name, color, data);
    }

    @Override
    public boolean isCompleted(ITriggerData iTriggerData) {
        int count = ((DataCount) iTriggerData).count;
        if (greaterThan && count > amount) return true;
        if (isEqualTo && count == amount) return true;
        if (lesserThan && count < amount) return true;
        return false;
    }
}
