package joshie.progression.asm.helpers;

public class ThaumcraftHelper {
    /** Code adapted from thaumcraft to fit my purposes **/
    /*
    public static void onContainerChanged(Container container, TileArcaneWorkbench tile, InventoryPlayer ip) {
        InventoryCrafting ic = new InventoryCrafting(new ContainerDummy(), 3, 3);
        for (int a = 0; a < 9; a++) { //Validate all the items can be used in crafting
            ItemStack stack = tile.inventory.getStackInSlot(a);
            if (!CraftingHelper.canPerformAction(ActionType.CRAFTINGUSE, ip.player, stack) || !CraftingHelper.canPerformAction(ActionType.ARCANEUSE, ip.player, stack)) {
                ic.setInventorySlotContents(a, null);
            } else ic.setInventorySlotContents(a, stack);
        }

        ItemStack result = CraftingHelper.getCraftingResult(ActionType.CRAFTING, ip.player, CraftingManager.getInstance().findMatchingRecipe(ic, tile.getWorld()));
        tile.inventory.setInventorySlotContentsSoftly(9, result);
        if ((tile.inventory.getStackInSlot(9) == null) && (tile.inventory.getStackInSlot(10) != null) && ((tile.inventory.getStackInSlot(10).getItem() instanceof IWand))) {
            IWand wand = (IWand) tile.inventory.getStackInSlot(10).getItem();
            ItemStack arcane = CraftingHelper.getCraftingResult(ActionType.ARCANE, ip.player, ThaumcraftCraftingManager.findMatchingArcaneRecipe(tile.inventory, ip.player));
            if (arcane != null) {
                if (wand.consumeAllVis(tile.inventory.getStackInSlot(10), ip.player, ThaumcraftCraftingManager.findMatchingArcaneRecipeAspects(tile.inventory, ip.player), false, true)) {
                    tile.inventory.setInventorySlotContentsSoftly(9, arcane);
                }
            }
        }

        tile.markDirty();
        tile.getWorld().markBlockForUpdate(tile.getPos());
        container.detectAndSendChanges();
    } */
}
