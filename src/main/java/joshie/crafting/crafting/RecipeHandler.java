package joshie.crafting.crafting;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import joshie.crafting.api.crafting.CraftingEvent.CraftingType;
import joshie.crafting.asm.ContainerPlayer;
import joshie.crafting.asm.ContainerTile;
import joshie.crafting.helpers.ClientHelper;
import joshie.crafting.helpers.CraftingHelper;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.ReflectionHelper;

public class RecipeHandler {
    public static Field fContainer;
    public static Field fCrafters;

    //Initialise all the fields
    static {
        try {
            fContainer = ReflectionHelper.findField(InventoryCrafting.class, "eventHandler", "field_70465_c", "c");
            fCrafters = ReflectionHelper.findField(Container.class, "crafters", "field_75149_d", "e");
        } catch (Exception e) {}
    }

    public static List<Object> getPlayers(InventoryCrafting crafting, boolean isClient) {
        if (isClient && ClientHelper.getPlayer() != null) return Arrays.asList(new Object[] { ClientHelper.getPlayer() });
        try {
            Container container = (Container) fContainer.get(crafting);
            List list = (List) fCrafters.get(container);
            List<Object> uuids = new ArrayList();
            uuids.addAll(list);
            if (list.size() < 1) {
                if (container instanceof ContainerPlayer) {
                    uuids.add(((ContainerPlayer) container).player);
                } else if (container instanceof ContainerTile) {
                    uuids.add(((ContainerTile) container).tile);
                }
            }

            return uuids;
        } catch (Exception e) {}

        return new ArrayList(); //Return an empty list
    }

    /** Called when trying to find a recipe **/
    public static ItemStack findMatchingRecipe(InventoryCrafting crafting, World world) {   
        if (world == null) return null; //Return null if the world is null
        
        ItemStack itemstack = null, itemstack1 = null;
        int slot = 0;
        for (int j = 0; j < crafting.getSizeInventory(); j++) {
            ItemStack itemstack2 = crafting.getStackInSlot(j);
            if (itemstack2 != null) {
                if (slot == 0) itemstack = itemstack2;
                if (slot == 1) itemstack1 = itemstack2;

                slot++;
            }
        }

        List<Object> objects = getPlayers(crafting, world.isRemote);
        for (Object object : objects) {
            for (int i = 0; i < crafting.getSizeInventory(); i++) {
                ItemStack stack = crafting.getStackInSlot(i);
                if (!CraftingHelper.canUseItemForCrafting(CraftingType.CRAFTING, object, stack)) return null;
            }
            
            //If we are repairing items, check if the crafter can repair it
            if (slot == 2 && CraftingHelper.canRepairItem(object, itemstack) && itemstack.getItem() == itemstack1.getItem() && itemstack.stackSize == 1 && itemstack1.stackSize == 1 && itemstack.getItem().isRepairable()) {
                Item item = itemstack.getItem();
                int j1 = item.getMaxDamage() - itemstack.getItemDamageForDisplay();
                int k = item.getMaxDamage() - itemstack1.getItemDamageForDisplay();
                int l = j1 + k + item.getMaxDamage() * 5 / 100;
                int i1 = item.getMaxDamage() - l;

                if (i1 < 0) {
                    i1 = 0;
                }

                return new ItemStack(itemstack.getItem(), 1, i1);
            } else {
                //Check the recipes
                for (int j = 0; j < CraftingManager.getInstance().getRecipeList().size(); ++j) {
                    IRecipe irecipe = (IRecipe) CraftingManager.getInstance().getRecipeList().get(j);
                    if (irecipe.matches(crafting, world)) {
                        ItemStack stack = irecipe.getCraftingResult(crafting);
                        if (CraftingHelper.canCraftItem(CraftingType.CRAFTING, object, stack)) { //If we are permitted to craft this, then allow it
                            return stack;
                        }
                    }
                }
            }
        }

        return null;
    }
}
