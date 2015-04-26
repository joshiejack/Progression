package joshie.progression.crafting;

import net.minecraft.item.ItemStack;

public class CrafterCreative extends Crafter {
	public static CrafterCreative INSTANCE = new CrafterCreative();
	private CrafterCreative() {}
	
	@Override
	public boolean canUseItemForCrafting(ActionType type, ItemStack stack) {
		return true;
	}

	@Override
	public boolean canCraftItem(ActionType type, ItemStack stack) {
		return true;
	}

	@Override
	public boolean canCraftWithAnything() {
		return true;
	}
}
