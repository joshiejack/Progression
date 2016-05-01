package joshie.progression.crafting.actions;

import joshie.progression.crafting.ActionType;
import joshie.progression.handlers.ProgressionEvents;
import joshie.progression.helpers.BlockActionHelper;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ActionBreakBlock extends ActionForgeEvent {
    public static final ActionBreakBlock INSTANCE = new ActionBreakBlock();

    @SubscribeEvent
    public void onBreakSpeed(BreakSpeed event) {
        EntityPlayer player = event.getEntityPlayer();
        if (player != null) {
            Block block = event.getState().getBlock();
            int meta = block.getMetaFromState(event.getState());
            if (ProgressionEvents.isEventCancelled(event.getEntityPlayer(), ActionType.BREAKBLOCKWITH, event.getEntityPlayer().getHeldItemMainhand(), ActionType.BREAKBLOCK, BlockActionHelper.getStackFromBlockData(block, meta))) {
                event.setNewSpeed(0F);
            }
        }
    }

    @SubscribeEvent
    public void onBreakBlock(BreakEvent event) {
        EntityPlayer player = event.getPlayer();
        if (player != null) {
            Block block = event.getState().getBlock();
            int meta = block.getMetaFromState(event.getState());
            if (ProgressionEvents.isEventCancelled(player, ActionType.BREAKBLOCKWITH, player.getHeldItemMainhand(), ActionType.BREAKBLOCK, BlockActionHelper.getStackFromBlockData(block, meta))) {
                event.setCanceled(true);
            }
        }
    }
}
