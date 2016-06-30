package joshie.progression.asm.helpers;

import joshie.progression.asm.ContainerPlayer;
import joshie.progression.asm.ContainerTile;
import joshie.progression.crafting.ActionType;
import joshie.progression.helpers.CraftingHelper;
import joshie.progression.helpers.MCClientHelper;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VanillaHelper {
    public static final Field fContainer = ReflectionHelper.findField(InventoryCrafting.class, "eventHandler", "field_70465_c", "d");
    public static final Field fCrafters = ReflectionHelper.findField(Container.class, "listeners", "field_75149_d", "e");

    //Initialise all the fields
    public static List<Object> getPlayers(InventoryCrafting crafting, boolean isClient) {
        if (isClient && MCClientHelper.getPlayer() != null) return Arrays.asList(new Object[] { MCClientHelper.getPlayer() });
        try {
            Container container = (Container) VanillaHelper.fContainer.get(crafting);
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList(); //Return an empty list
    }

    public static void onContainerChanged(InventoryCrafting matrix, IInventory inventory, World world) {
        if (world == null) return; //Why would the world be null!
        ItemStack result = CraftingManager.getInstance().findMatchingRecipe(matrix, world);
        if (result != null) {
            for (Object o : getPlayers(matrix, world.isRemote)) {
                if (!CraftingHelper.canPerformActionAbstract(ActionType.CRAFTING, o, result)) {
                    result = null;
                }

                if (result != null) {
                    for (int i = 0; i < matrix.getSizeInventory(); i++) {
                        if (matrix.getStackInSlot(i) != null) {
                            ItemStack stack = matrix.getStackInSlot(i);
                            if (!CraftingHelper.canPerformActionAbstract(ActionType.CRAFTINGUSE, o, stack)) {
                                result = null;
                                break;
                            }
                        }
                    }
                }
            }
        }

        inventory.setInventorySlotContents(0, result);
    }
}
