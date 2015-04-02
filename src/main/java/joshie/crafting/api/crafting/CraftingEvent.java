package joshie.crafting.api.crafting;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
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
        public static final List<CraftingType> craftingTypes = new ArrayList();
        public static final CraftingType CRAFTING = new CraftingType("Crafting", "crafting");
        public static final CraftingType FURNACE = new CraftingType("Furnace", "furnace");
        public static final CraftingType BREAKBLOCK = new CraftingType("Break Block", "breakblock");
        public static final CraftingType HARVEST = new CraftingType("Harvest Drop", "harvestdrop");
        public static final CraftingType ENTITY = new CraftingType("Entity Drop", "entitydrop");
        public int id;
        public String name;
        public String display;
        
        public CraftingType(String display, String name) {
            this.display = display;
            this.name = name;
            this.id = craftingTypes.size();
            craftingTypes.add(this);
        }

        public String getDisplayName() {
            return display;
        }
    }
}
