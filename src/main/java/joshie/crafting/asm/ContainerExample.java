/*******************************************************************************
 * Copyright (c) 2011-2014 SirSengir.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v3
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Various Contributors including, but not limited to:
 * SirSengir (original work), CovertJaguar, Player, Binnie, MysteriousAges
 ******************************************************************************/
package joshie.crafting.asm;

import joshie.crafting.asm.helpers.AutopackagerHelper;
import joshie.crafting.asm.helpers.ForestryHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.tileentity.TileEntity;
import forestry.core.interfaces.ICrafter;
import forestry.core.inventory.InvTools;
import forestry.core.inventory.InventoryAdapter;
import forestry.core.inventory.TileInventoryAdapter;
import forestry.core.proxy.Proxies;
import forestry.core.utils.RecipeUtil;
import forestry.factory.recipes.RecipeMemory;

public class ContainerExample extends TileEntity implements ICrafter {

	/* CONSTANTS */
	public final static int SLOT_CRAFTING_1 = 0;
	public final static int SLOT_CRAFTING_COUNT = 9;
	public final static int SLOT_CRAFTING_RESULT = 9;
	public final static short SLOT_INVENTORY_1 = 0;
	public final static short SLOT_INVENTORY_COUNT = 18;

	/* MEMBERS */
	private RecipeMemory.Recipe currentRecipe;
	private InventoryCrafting currentCrafting;
	private RecipeMemory memorized;
	private TileInventoryAdapter craftingInventory;

	public ContainerExample() {
		
		memorized = new RecipeMemory();
	}

	/* RECIPE SELECTION */
	public RecipeMemory getMemory() {
		return memorized;
	}

	public void chooseRecipe(int recipeIndex) {
		if (recipeIndex >= memorized.capacity) {
			for (int slot = 0; slot < craftingInventory.getSizeInventory(); slot++) {
				craftingInventory.setInventorySlotContents(slot, null);
			}
			return;
		}

		IInventory matrix = memorized.getRecipeMatrix(recipeIndex);
		if (matrix == null) {
			return;
		}

		for (int slot = 0; slot < matrix.getSizeInventory(); slot++) {
			craftingInventory.setInventorySlotContents(slot, matrix.getStackInSlot(slot));
		}
		
		InventoryCrafting largeCraft = new InventoryCrafting(null, 0, 0);
		
		ItemStack result = new ItemStack(Blocks.coal_block);
		result = AutopackagerHelper.getResult(this, largeCraft, result);
	}

	/* CRAFTING */
	public void setRecipe(InventoryCrafting crafting) {

		ItemStack recipeOutput = CraftingManager.getInstance().findMatchingRecipe(crafting, worldObj);
		if (recipeOutput == null) {
			currentRecipe = null;
			currentCrafting = null;
		} else {
			currentRecipe = new RecipeMemory.Recipe(crafting);
			currentCrafting = crafting;
		}
		updateCraftResult();
	}

	private void updateCraftResult() {
		if (currentRecipe != null) {
			ItemStack result = currentRecipe.getRecipeOutput(worldObj);
			if (result != null) {
				craftingInventory.setInventorySlotContents(SLOT_CRAFTING_RESULT, result.copy());
				return;
			}
		}

		craftingInventory.setInventorySlotContents(SLOT_CRAFTING_RESULT, null);
	}

	private boolean canCraftCurrentRecipe() {
		if (currentRecipe == null) {
			return false;
		}

		ItemStack[] recipeItems = InvTools.getStacks(craftingInventory, SLOT_CRAFTING_1, SLOT_CRAFTING_COUNT);
		//ItemStack[] inventory = InvTools.getStacks(getInternalInventory(), SLOT_INVENTORY_1, SLOT_INVENTORY_COUNT);
		ItemStack recipeOutput = currentRecipe.getRecipeOutput(worldObj);

		return RecipeUtil.canCraftRecipe(worldObj, recipeItems, recipeOutput, null);
	}

	private boolean removeResources(EntityPlayer player) {
		ItemStack[] set = InvTools.getStacks(craftingInventory, SLOT_CRAFTING_1, SLOT_CRAFTING_COUNT);
		return false;
		//return InvTools.removeSets(getInternalInventory(), 1, set, SLOT_INVENTORY_1, SLOT_INVENTORY_COUNT, player, true, true, true);
	}

	@Override
	public boolean canTakeStack(int slotIndex) {
		if (slotIndex == SLOT_CRAFTING_RESULT) {
			return canCraftCurrentRecipe();
		}
		return true;
	}
	
	public IInventory getInternalInventory() {
	    return null;
	}
	
	public boolean canTakeStack2(int slotIndex) {
	    return ForestryHelper.canCraftRecipe(this, craftingInventory, getInternalInventory(), currentRecipe, slotIndex) ;
    }

	@Override
	public ItemStack takenFromSlot(int slotIndex, EntityPlayer player) {
		if (!removeResources(player)) {
			return null;
		}

		if (Proxies.common.isSimulating(worldObj)) {
			memorized.memorizeRecipe(worldObj, currentRecipe, currentCrafting);
		}

		updateCraftResult();
		return currentRecipe.getRecipeOutput(worldObj).copy();
	}

	@Override
	public ItemStack getResult() {
		if (currentRecipe == null) {
			return null;
		}

		if (currentRecipe.getRecipeOutput(worldObj) != null) {
			return currentRecipe.getRecipeOutput(worldObj).copy();
		}
		return null;
	}

	/**
	 * @return Inaccessible crafting inventory for the craft grid.
	 */
	public InventoryAdapter getCraftingInventory() {
		return craftingInventory;
	}
}
