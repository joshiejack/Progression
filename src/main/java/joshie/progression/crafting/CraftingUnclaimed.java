package joshie.progression.crafting;

import joshie.progression.json.Options;
import net.minecraft.item.ItemStack;

/** This class is returned when machines look for their owners
 *  And they cannot be find. */
public class CraftingUnclaimed extends Crafter {
	public static final Crafter INSTANCE = new CraftingUnclaimed();
	
	@Override
	public boolean canUseItemForCrafting(CraftingType type, ItemStack stack) {
		return Options.unclaimedTileCanUseAnythingForCrafting;
	}

	@Override
	public boolean canCraftItem(CraftingType type, ItemStack stack) {
		return Options.unclaimedTileCanCraftAnything;
	}

	@Override
	public boolean canCraftWithAnything() {
		return false;
	}

	@Override
	public boolean canRepairItem(ItemStack stack) {
		return false;
	}
}
