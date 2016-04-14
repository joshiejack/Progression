package joshie.progression.crafting;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class CrafterCreative extends Crafter {
    public static CrafterCreative INSTANCE = new CrafterCreative();
    private CrafterCreative() {}

    @Override
    public boolean canUseItemWithAction(World world,  ActionType type, ItemStack stack) {
        return true;
    }

    @Override
    public boolean canDoAnything() {
        return true;
    }
}
