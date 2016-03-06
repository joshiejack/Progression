package joshie.progression.asm.helpers;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import joshie.progression.asm.ContainerPlayer;
import joshie.progression.asm.ContainerTile;
import joshie.progression.crafting.ActionType;
import joshie.progression.helpers.ClientHelper;
import joshie.progression.helpers.CraftingHelper;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;

public class VanillaHelper {
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList(); //Return an empty list
    }

    public static void onContainerChanged(InventoryCrafting matrix, IInventory inventory, World world) {
        ItemStack result = CraftingManager.getInstance().findMatchingRecipe(matrix, world);
        if (result != null) {
            for (Object o : getPlayers(matrix, FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)) {
                if (!CraftingHelper.canCraftItem(ActionType.CRAFTING, o, result)) {
                    result = null;
                }

                if (result != null) {
                    for (int i = 0; i < matrix.getSizeInventory(); i++) {
                        if (matrix.getStackInSlot(i) != null) {
                            ItemStack stack = matrix.getStackInSlot(i);
                            if (!CraftingHelper.canUseItemForCrafting(ActionType.CRAFTING, o, stack)) {
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
