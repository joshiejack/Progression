package joshie.progression.criteria.triggers;

import joshie.progression.api.criteria.IProgressionTriggerData;
import joshie.progression.criteria.triggers.data.DataCount;
import net.minecraft.item.ItemStack;

public abstract class TriggerBaseCounterVaries extends TriggerBaseCounter {
    public boolean greaterThan = true;
    public boolean isEqualTo = true;
    public boolean lesserThan = false;
    
    public TriggerBaseCounterVaries(ItemStack stack, String name, int color) {
        super(stack, name, color, "count");
    }

    @Override
    public boolean isCompleted(IProgressionTriggerData iTriggerData) {
        int count = ((DataCount) iTriggerData).count;
        if (greaterThan && count > amount) return true;
        if (isEqualTo && count == amount) return true;
        if (lesserThan && count < amount) return true;
        return false;
    }
}
