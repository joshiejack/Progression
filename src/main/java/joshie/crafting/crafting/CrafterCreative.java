package joshie.crafting.crafting;

import joshie.crafting.api.crafting.CraftingType;
import joshie.crafting.api.crafting.ICrafter;
import net.minecraft.item.ItemStack;

public class CrafterCreative implements ICrafter {
	@Override
	public boolean canUseItemForCrafting(ItemStack stack) {
		return true;
	}

	@Override
	public boolean canCraftItem(CraftingType type, ItemStack stack) {
		return true;
	}

	@Override
	public boolean canCraftWithAnything() {
		return true;
	}

	@Override
	public boolean canRepairItem(ItemStack stack) {
		return true;
	}
}
