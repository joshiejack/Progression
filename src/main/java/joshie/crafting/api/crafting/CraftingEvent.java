package joshie.crafting.api.crafting;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import cpw.mods.fml.common.eventhandler.Cancelable;
import cpw.mods.fml.common.eventhandler.Event;

@Cancelable
public class CraftingEvent extends Event {
    /** Can be null **/
    public final EntityPlayer player;
    /** Can be null **/
    public final TileEntity tile;
    public final ItemStack stack;

    public CraftingEvent(EntityPlayer player, ItemStack stack) {
        this.player = player;
        this.tile = null;
        this.stack = stack;
    }
    
    public CraftingEvent(TileEntity tile, ItemStack stack) {
        this.player = null;
        this.tile = tile;
        this.stack = stack;
    }

    @Cancelable
    public static class CanCraftItemEvent extends CraftingEvent {
        public final CraftingType type;
        
        public CanCraftItemEvent(CraftingType type, EntityPlayer player, ItemStack stack) {
            super(player, stack);
            this.type = type;
        }
        
        public CanCraftItemEvent(CraftingType type, TileEntity tile, ItemStack stack) {
            super(tile, stack);
            this.type = type;
        }
    }

    /** The stack can be null with this event **/
    @Cancelable
    public static class CanUseItemCraftingEvent extends CraftingEvent {
        public final CraftingType type;
        
        public CanUseItemCraftingEvent(CraftingType type, EntityPlayer player, ItemStack stack) {
            super(player, stack);
            this.type = type;
        }
        
        public CanUseItemCraftingEvent(CraftingType type, TileEntity tile, ItemStack stack) {
            super(tile, stack);
            this.type = type;
        }
    }
    
    @Cancelable
    public static class CanRepairItemEvent extends CraftingEvent {
        public CanRepairItemEvent(EntityPlayer player, ItemStack stack) {
            super(player, stack);
        }
        
        public CanRepairItemEvent(TileEntity tile, ItemStack stack) {
            super(tile, stack);
        }
    }
    
    public static class CraftingType {
        public static final Set<CraftingType> craftingTypes = new HashSet();
        public static final CraftingType CRAFTING = new CraftingType("crafting");
        public static final CraftingType FURNACE = new CraftingType("furnace");
        public String name;
        
        public CraftingType(String name) {
            this.name = name;
            craftingTypes.add(this);
        }
    }
}
