package joshie.progression.helpers;

import joshie.progression.api.ActionEvent.CanObtainFromActionEvent;
import joshie.progression.api.ActionEvent.CanUseToPeformActionEvent;
import joshie.progression.crafting.ActionType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;

public class CraftingHelper {
    public static boolean canCraftItem(ActionType crafting, EntityPlayer player, ItemStack stack) {
        return !MinecraftForge.EVENT_BUS.post(new CanObtainFromActionEvent(crafting, player, stack));
    }
    
    public static boolean canCraftItem(ActionType crafting, TileEntity tile, ItemStack stack) {
        return !MinecraftForge.EVENT_BUS.post(new CanObtainFromActionEvent(crafting, tile, stack));
    }
    
    public static boolean canCraftItem(ActionType crafting, Object object, ItemStack stack) {
        if (stack == null) return false;
        
        EntityPlayer player = object instanceof EntityPlayer ? (EntityPlayer) object: null;
        if (player != null) {
            return canCraftItem(crafting, player, stack);
        }
        
        TileEntity tile = object instanceof TileEntity? (TileEntity) object: null;
        if (tile != null) {
            return canCraftItem(crafting, tile, stack);
        }
                
        return false;
    }
    
    public static boolean canUseItemForCrafting(ActionType crafting, EntityPlayer player, ItemStack stack) {
        return !MinecraftForge.EVENT_BUS.post(new CanUseToPeformActionEvent(crafting, player, stack));
    }
    
    public static boolean canUseItemForCrafting(ActionType crafting, TileEntity tile, ItemStack stack) {
        return !MinecraftForge.EVENT_BUS.post(new CanUseToPeformActionEvent(crafting, tile, stack));
    }
    
    public static boolean canUseItemForCrafting(ActionType crafting, Object object, ItemStack stack) {        
        EntityPlayer player = object instanceof EntityPlayer ? (EntityPlayer) object: null;
        if (player != null) {
            return canUseItemForCrafting(crafting, player, stack);
        }
                
        TileEntity tile = object instanceof TileEntity? (TileEntity) object: null;
        if (tile != null) {
            return canUseItemForCrafting(crafting, tile, stack);
        }
                
        return false;
    }
}
