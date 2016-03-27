package joshie.progression.criteria.rewards;

import net.minecraft.item.ItemStack;

public class RewardBaseSingular extends RewardBase {
    public RewardBaseSingular(ItemStack stack, String name, int color) {
        super(stack, name, color);
    }

    @Override
    public boolean shouldRunOnce() {
        return true;
    }
}
