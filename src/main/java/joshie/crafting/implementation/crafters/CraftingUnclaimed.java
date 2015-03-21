package joshie.crafting.implementation.crafters;

import joshie.crafting.api.crafting.CraftingType;
import joshie.crafting.api.crafting.ICrafter;
import net.minecraft.item.ItemStack;

/** This class is returned when machines look for their owners
 *  And they cannot be find. */
public class CraftingUnclaimed implements ICrafter {
	public static final ICrafter INSTANCE = new CraftingUnclaimed();
	
	@Override
	public boolean canUseItemForCrafting(ItemStack stack) {
		return false;
	}

	@Override
	public boolean canCraftItem(CraftingType type, ItemStack stack) {
		return false;
	}

	@Override
	public boolean canCraftWithAnything() {
		return true;
	}

	@Override
	public boolean canRepairItem(ItemStack stack) {
		return false;
	}
}
