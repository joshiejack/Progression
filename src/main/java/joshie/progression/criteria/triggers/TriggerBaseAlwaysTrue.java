package joshie.progression.criteria.triggers;

import net.minecraft.item.ItemStack;

import java.util.UUID;

public abstract class TriggerBaseAlwaysTrue extends TriggerBase {
    public TriggerBaseAlwaysTrue(ItemStack stack, String name, int color) {
        super(stack, name, color);
    }

    @Override
    public boolean isCompleted() {
        return true;
    }

    @Override
    public boolean onFired(UUID uuid, Object... data) {
        return true;
    }
}