package joshie.crafting.plugins.minetweaker;

import minetweaker.api.item.IIngredient;
import minetweaker.api.minecraft.MineTweakerMC;
import net.minecraft.item.ItemStack;

public class MTHelper {
	public static ItemStack getItemStack(IIngredient stack) {
		return MineTweakerMC.getItemStack(stack); 
	}
}
