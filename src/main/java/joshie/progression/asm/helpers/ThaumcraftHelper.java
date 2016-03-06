package joshie.progression.asm.helpers;

import joshie.progression.crafting.ActionType;
import joshie.progression.helpers.CraftingHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;

public class ThaumcraftHelper {
	//TODO: Thaumcraft
    /** Code adapted from thaumcraft to fit my purposes **/
    /*public static void onContainerChanged(TileArcaneWorkbench tile, InventoryPlayer ip) {
        InventoryCrafting craftMatrix = new InventoryCrafting(new ContainerDummy(), 3, 3);
        for (int a = 0; a < 9; a++) {
            ItemStack stack = tile.getStackInSlot(a);
            if (!CraftingHelper.canUseItemForCrafting(ActionType.CRAFTING, ip.player, stack)) {
                craftMatrix.setInventorySlotContents(a, null);
            } else craftMatrix.setInventorySlotContents(a, stack);
        }

        ItemStack result = CraftingManager.getInstance().findMatchingRecipe(craftMatrix, tile.getWorldObj());
        if (result != null) {
            if (!CraftingHelper.canCraftItem(ActionType.CRAFTING, ip.player, result)) {
                result = null;
            }
        }

        tile.setInventorySlotContentsSoftly(9, result);
        if ((tile.getStackInSlot(9) == null) && (tile.getStackInSlot(10) != null) && ((tile.getStackInSlot(10).getItem() instanceof ItemWandCasting))) {
            ItemWandCasting wand = (ItemWandCasting) tile.getStackInSlot(10).getItem();
            if (wand.consumeAllVisCrafting(tile.getStackInSlot(10), ip.player, ThaumcraftCraftingManager.findMatchingArcaneRecipeAspects(tile, ip.player), false)) {
                tile.setInventorySlotContentsSoftly(9, ThaumcraftCraftingManager.findMatchingArcaneRecipe(tile, ip.player));
            }
        }
    } */
}
