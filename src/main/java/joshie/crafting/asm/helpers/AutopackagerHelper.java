package joshie.crafting.asm.helpers;

import joshie.crafting.api.crafting.CraftingEvent.CraftingType;
import joshie.crafting.helpers.CraftingHelper;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class AutopackagerHelper {
    public static ItemStack getResult(TileEntity tile, InventoryCrafting matrix, ItemStack result) {
        if (result == null) return null;
        if (!CraftingHelper.canCraftItem(CraftingType.CRAFTING, tile, result)) return null;
        for (int i = 0; i < matrix.getSizeInventory(); i++) {
            if (matrix.getStackInSlot(i) != null) {
                ItemStack stack = matrix.getStackInSlot(i);
                if (!CraftingHelper.canUseItemForCrafting(CraftingType.CRAFTING, tile, stack)) {
                    return null;
                }
            }
        }

        return result;
    }
}
