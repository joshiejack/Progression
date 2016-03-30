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
    public final ActionType type;

    public ActionEvent(ActionType type, EntityPlayer player, ItemStack stack) {
        this.type = type;
        this.player = player;
        this.tile = null;
        this.stack = stack;
    }
    
    public ActionEvent(ActionType type, TileEntity tile, ItemStack stack) {
        this.type = type;
        this.player = null;
        this.tile = tile;
        this.stack = stack;
    }
}
