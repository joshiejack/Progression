package joshie.crafting.crafting;

import joshie.crafting.api.crafting.ICrafter;
import joshie.crafting.api.crafting.CraftingEvent.CraftingType;
import net.minecraft.item.ItemStack;

public class CrafterCreative implements ICrafter {
	public static CrafterCreative INSTANCE = new CrafterCreative();
	private CrafterCreative() {}
	
	@Override
	public boolean canUseItemForCrafting(CraftingType type, ItemStack stack) {
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
