package joshie.progression.criteria.triggers;

import net.minecraft.item.ItemStack;

public abstract class TriggerBaseCounterVaries extends TriggerBaseCounter {
    public boolean greaterThan = true;
    public boolean isEqualTo = true;
    public boolean lesserThan = false;
    
    public TriggerBaseCounterVaries(ItemStack stack, String name, int color) {
        super(stack, name, color);
    }

    @Override
    public boolean isCompleted() {
        if (greaterThan && counter > amount) return true;
        if (isEqualTo && counter == amount) return true;
        if (lesserThan && counter < amount) return true;
        return false;
    }
}
