package joshie.progression.api;

import joshie.progression.crafting.ActionType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import cpw.mods.fml.common.eventhandler.Cancelable;
import cpw.mods.fml.common.eventhandler.Event;

@Cancelable
public class ActionEvent extends Event {
    /** Can be null **/
    public final EntityPlayer player;
    /** Can be null **/
    public final TileEntity tile;
    public final ItemStack stack;

    public ActionEvent(EntityPlayer player, ItemStack stack) {
        this.player = player;
        this.tile = null;
        this.stack = stack;
    }
    
    public ActionEvent(TileEntity tile, ItemStack stack) {
        this.player = null;
        this.tile = tile;
        this.stack = stack;
    }

    @Cancelable
    public static class CanObtainFromActionEvent extends ActionEvent {
        public final ActionType type;
        
        public CanObtainFromActionEvent(ActionType type, EntityPlayer player, ItemStack stack) {
            super(player, stack);
            this.type = type;
        }
        
        public CanObtainFromActionEvent(ActionType type, TileEntity tile, ItemStack stack) {
            super(tile, stack);
            this.type = type;
        }
    }

    /** The stack can be null with this event **/
    @Cancelable
    public static class CanUseToPeformActionEvent extends ActionEvent {
        public final ActionType type;
        
        public CanUseToPeformActionEvent(ActionType type, EntityPlayer player, ItemStack stack) {
            super(player, stack);
            this.type = type;
        }
        
        public CanUseToPeformActionEvent(ActionType type, TileEntity tile, ItemStack stack) {
            super(tile, stack);
            this.type = type;
        }
    }
}
