package joshie.crafting.crafting;

import joshie.crafting.api.crafting.CraftingEvent.CraftingType;
import joshie.crafting.api.crafting.ICrafter;
import joshie.crafting.json.Options;
import net.minecraft.item.ItemStack;

/** This class is returned when machines look for their owners
 *  And they cannot be find. */
public class CraftingUnclaimed implements ICrafter {
	public static final ICrafter INSTANCE = new CraftingUnclaimed();
	
	@Override
	public boolean canUseItemForCrafting(CraftingType type, ItemStack stack) {
		return Options.settings.unclaimedTileCanUseAnythingForCrafting;
	}

	@Override
	public boolean canCraftItem(CraftingType type, ItemStack stack) {
		return Options.settings.unclaimedTileCanCraftAnything;
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
