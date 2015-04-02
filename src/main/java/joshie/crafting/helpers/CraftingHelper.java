package joshie.crafting.helpers;

import joshie.crafting.api.crafting.CraftingEvent.CanCraftItemEvent;
import joshie.crafting.api.crafting.CraftingEvent.CanRepairItemEvent;
import joshie.crafting.api.crafting.CraftingEvent.CanUseItemCraftingEvent;
import joshie.crafting.api.crafting.CraftingEvent.CraftingType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;

public class CraftingHelper {
    public static boolean canCraftItem(CraftingType crafting, EntityPlayer player, ItemStack stack) {
        return !MinecraftForge.EVENT_BUS.post(new CanCraftItemEvent(crafting, player, stack));
    }
    
    public static boolean canCraftItem(CraftingType crafting, TileEntity tile, ItemStack stack) {
        return !MinecraftForge.EVENT_BUS.post(new CanCraftItemEvent(crafting, tile, stack));
    }
    
    public static boolean canCraftItem(CraftingType crafting, Object object, ItemStack stack) {
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
    
    public static boolean canUseItemForCrafting(CraftingType crafting, EntityPlayer player, ItemStack stack) {
        return !MinecraftForge.EVENT_BUS.post(new CanUseItemCraftingEvent(crafting, player, stack));
    }
    
    public static boolean canUseItemForCrafting(CraftingType crafting, TileEntity tile, ItemStack stack) {
        return !MinecraftForge.EVENT_BUS.post(new CanUseItemCraftingEvent(crafting, tile, stack));
    }
    
    public static boolean canUseItemForCrafting(CraftingType crafting, Object object, ItemStack stack) {        
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
    
    public static boolean canRepairItem(Object object, ItemStack stack) {
        if (stack == null) return false;
        
        EntityPlayer player = object instanceof EntityPlayer ? (EntityPlayer) object: null;
        if (player != null) {
            return !MinecraftForge.EVENT_BUS.post(new CanRepairItemEvent(player, stack));
        }
        
        TileEntity tile = object instanceof TileEntity? (TileEntity) object: null;
        if (tile != null) {
            return !MinecraftForge.EVENT_BUS.post(new CanRepairItemEvent(tile, stack));
        }
        
        return false;
    }
}
