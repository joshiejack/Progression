package joshie.progression.api.event;

import joshie.progression.api.IProgressionAPI;
import joshie.progression.crafting.ActionType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

/** You can call this directly, but this is recommended that you
 *  use the helper method that is found in {@link IProgressionAPI }
 *  recommended usage would be :
 *  
 *  ProgressionAPI.registry.canObtainFromAction("crafting", new ItemStack(Blocks.stone), player);
 *  Obviously if you want to listen to the events for any reason, ignore that ^ */
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
