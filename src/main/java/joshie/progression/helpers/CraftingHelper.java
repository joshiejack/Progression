package joshie.progression.helpers;

import joshie.progression.api.event.ActionEvent;
import joshie.progression.crafting.ActionType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;

public class CraftingHelper {
    public static boolean canPerformAction(ActionType action, EntityPlayer player, ItemStack stack) {
        return !MinecraftForge.EVENT_BUS.post(new ActionEvent(action, player, stack));
    }

    public static boolean canPerformAction(ActionType action, TileEntity tile, ItemStack stack) {
        return !MinecraftForge.EVENT_BUS.post(new ActionEvent(action, tile, stack));
    }

    public static boolean canPerformActionAbstract(ActionType crafting, Object object, ItemStack stack) {
        if (stack == null) return false;
        
        EntityPlayer player = object instanceof EntityPlayer ? (EntityPlayer) object: null;
        if (player != null) {
            return canPerformAction(crafting, player, stack);
        }
        
        TileEntity tile = object instanceof TileEntity? (TileEntity) object: null;
        if (tile != null) {
            return canPerformAction(crafting, tile, stack);
        }
                
        return false;
    }
    
    public static ItemStack getCraftingResult(ActionType crafting, EntityPlayer player, ItemStack result) {
        if (result == null) return null;
        if (!CraftingHelper.canPerformAction(ActionType.CRAFTING, player, result)) return null;
        return result;
    }
}
