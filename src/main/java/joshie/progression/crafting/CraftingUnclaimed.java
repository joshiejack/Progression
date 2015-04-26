package joshie.progression.crafting;

import joshie.progression.json.Options;
import net.minecraft.item.ItemStack;

/** This class is returned when machines look for their owners
 *  And they cannot be find. */
public class CraftingUnclaimed extends Crafter {
	public static final Crafter INSTANCE = new CraftingUnclaimed();
	
	@Override
	public boolean canUseItemForCrafting(ActionType type, ItemStack stack) {
		return Options.unclaimedTileCanUseAnythingForCrafting;
	}

	@Override
	public boolean canCraftItem(ActionType type, ItemStack stack) {
		return Options.unclaimedTileCanCraftAnything;
	}

	@Override
	public boolean canCraftWithAnything() {
		return Options.unclaimedTileCanUseAnythingForCrafting;
	}

    @Override
    public boolean canCraftAnything() {
        return Options.unclaimedTileCanCraftAnything;
    }
}
