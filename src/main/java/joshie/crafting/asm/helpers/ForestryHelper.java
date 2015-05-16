package joshie.crafting.asm.helpers;

import joshie.crafting.api.crafting.CraftingEvent.CraftingType;
import joshie.crafting.helpers.CraftingHelper;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import forestry.core.inventory.InvTools;
import forestry.core.inventory.TileInventoryAdapter;
import forestry.core.utils.RecipeUtil;
import forestry.factory.gadgets.TileWorktable;
import forestry.factory.recipes.RecipeMemory.Recipe;

public class ForestryHelper {
    public static boolean canCraftRecipe(TileEntity tile, TileInventoryAdapter craftingInventory, IInventory internalInventory, Recipe currentRecipe, int slotIndex) {
        if (slotIndex == TileWorktable.SLOT_CRAFTING_RESULT) {
            if (currentRecipe == null) {
                return false;
            }

            ItemStack[] recipeItems = InvTools.getStacks(craftingInventory, TileWorktable.SLOT_CRAFTING_1, TileWorktable.SLOT_CRAFTING_COUNT);
            ItemStack[] inventory = InvTools.getStacks(internalInventory, TileWorktable.SLOT_INVENTORY_1, TileWorktable.SLOT_INVENTORY_COUNT);
            ItemStack recipeOutput = currentRecipe.getRecipeOutput(tile.getWorldObj());

            boolean canCraft = RecipeUtil.canCraftRecipe(tile.getWorldObj(), recipeItems, recipeOutput, inventory);
            if (canCraft) {
                if (!CraftingHelper.canCraftItem(CraftingType.CRAFTING, tile, recipeOutput)) return false;
                //Now to validate the contents
                for (ItemStack ingredient: recipeItems) {
                    if (!CraftingHelper.canUseItemForCrafting(CraftingType.CRAFTING, tile, ingredient)) return false;
                }
                
                return true;
            } else return false;
        } else return true;
    }
}
