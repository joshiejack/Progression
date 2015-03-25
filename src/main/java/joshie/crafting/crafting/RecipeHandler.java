
package joshie.crafting.crafting;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import joshie.crafting.api.crafting.CraftingType;
import joshie.crafting.api.crafting.ICrafter;
import joshie.crafting.asm.ContainerPlayer;
import joshie.crafting.helpers.ClientHelper;
import joshie.crafting.helpers.PlayerHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import scala.actors.threadpool.Arrays;
import cpw.mods.fml.relauncher.ReflectionHelper;



public class RecipeHandler {	
	public static Field fContainer;
	public static Field fCrafters;
	
	//Initialise all the fields
	static {
		try {
			fContainer = ReflectionHelper.findField(InventoryCrafting.class, "eventHandler");
			fCrafters = ReflectionHelper.findField(Container.class, "crafters");
		} catch (Exception e) {}
	}
	
	public static List<UUID> getPlayers(InventoryCrafting crafting, boolean isClient) {
		if (isClient && ClientHelper.getPlayer() != null) return Arrays.asList(new Object[] { PlayerHelper.getUUIDForPlayer(ClientHelper.getPlayer()) });
		try {
			Container container = (Container) fContainer.get(crafting);
			List list = (List) fCrafters.get(container);
			List<UUID> uuids = new ArrayList();
			if (list.size() < 1) {
				if (container instanceof ContainerPlayer) {
					uuids.add(((ContainerPlayer)container).uuid);
				}
			}
			
			
			for (EntityPlayer player: (ArrayList<EntityPlayer>)list) {
				uuids.add(PlayerHelper.getUUIDForPlayer(player));
			}
			
			return uuids;
		} catch (Exception e) {}
		
		return new ArrayList(); //Return an empty list
	}
	
	/** Called when trying to find a recipe **/
	public static ItemStack findMatchingRecipe(InventoryCrafting crafting, World world) {			
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
				
		List<UUID> uuids = getPlayers(crafting, world.isRemote);
		for (UUID uuid: uuids) {
			ICrafter crafter = PlayerHelper.getCrafterForUUID(uuid);
			if (!crafter.canCraftWithAnything()) { //If we can't craft with every item, let's validate them all
				for (int i = 0; i < crafting.getSizeInventory(); i++) {
					ItemStack stack = crafting.getStackInSlot(i);
					if (stack != null && !crafter.canUseItemForCrafting(CraftingType.CRAFTING, stack)) return null; //If it can't be used, return null
				}
			}
			
			//If we are repairing items, check if the crafter can repair it
			if (slot == 2 && crafter.canRepairItem(itemstack) && itemstack.getItem() == itemstack1.getItem() && itemstack.stackSize == 1 && itemstack1.stackSize == 1 && itemstack.getItem().isRepairable()) {
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
	                IRecipe irecipe = (IRecipe)CraftingManager.getInstance().getRecipeList().get(j);
	                if (irecipe.matches(crafting, world)) {
	                	ItemStack stack = irecipe.getCraftingResult(crafting);
	                    if (crafter.canCraftItem(CraftingType.CRAFTING, stack)) { //If we are permitted to craft this, then allow it
	                    	return stack;
	                    }
	                }
	            }
	        }
		}
				
		return null;
	}
}
