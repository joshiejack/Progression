package joshie.progression.asm.helpers;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

//TODO: Tinkers
public class TConstructHelper {
    /*public static void onContainerChanged(InventoryCrafting matrix, IInventory inventory, World world) {
        ItemStack tool = modifyItem(matrix);
        if (tool != null) inventory.setInventorySlotContents(0, tool);
        else {
            VanillaHelper.onContainerChanged(matrix, inventory, world);
        }
    }

    public static ItemStack modifyItem(InventoryCrafting matrix) {
        ItemStack input = matrix.getStackInSlot(4);
        if (input != null) {
            Item item = input.getItem();
            if (item instanceof IModifyable) {
                ItemStack[] slots = new ItemStack[8];
                for (int i = 0; i < 4; i++) {
                    slots[i] = matrix.getStackInSlot(i);
                    slots[i + 4] = matrix.getStackInSlot(i + 5);
                }
                ItemStack output = ModifyBuilder.instance.modifyItem(input, slots);
                if (output != null) return output;
            }
        }
        return null;
    } */
}
