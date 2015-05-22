package joshie.progression.asm.helpers;

import joshie.progression.crafting.ActionType;
import joshie.progression.helpers.CraftingHelper;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class AutopackagerHelper {
    public static ItemStack getResult(TileEntity tile, InventoryCrafting matrix, ItemStack result) {
        if (result == null) return null;
        if (!CraftingHelper.canCraftItem(ActionType.CRAFTING, tile, result)) return null;
        for (int i = 0; i < matrix.getSizeInventory(); i++) {
            if (matrix.getStackInSlot(i) != null) {
                ItemStack stack = matrix.getStackInSlot(i);
                if (!CraftingHelper.canUseItemForCrafting(ActionType.CRAFTING, tile, stack)) {
                    return null;
                }
            }
        }

        return result;
    }
}