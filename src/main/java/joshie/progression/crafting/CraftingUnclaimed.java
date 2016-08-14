package joshie.progression.crafting;

import joshie.progression.json.Options;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/** This class is returned when machines look for their owners
 *  And they cannot be find. */
public class CraftingUnclaimed extends Crafter {
    public static final Crafter INSTANCE = new CraftingUnclaimed();

    @Override
    public boolean canUseItemWithAction(World world, ActionType type, ItemStack stack) {
        return Options.getSettings().unclaimedTileCanDoAnything;
    }

    @Override
    public boolean canDoAnything() {
        return Options.getSettings().unclaimedTileCanDoAnything;
    }
}
