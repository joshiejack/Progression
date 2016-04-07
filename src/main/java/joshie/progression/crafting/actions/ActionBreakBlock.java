package joshie.progression.crafting.actions;

import joshie.progression.crafting.ActionType;
import joshie.progression.handlers.CraftingEvents;
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
        Block block = event.state.getBlock();
        int meta = block.getMetaFromState(event.state);
        if (CraftingEvents.isEventCancelled(event.entityPlayer, ActionType.BREAKBLOCKWITH, event.entityPlayer.getCurrentEquippedItem(), ActionType.BREAKBLOCK, BlockActionHelper.getStackFromBlockData(block, meta))) {
            event.newSpeed = 0F;
        }
    }

    @SubscribeEvent
    public void onBreakBlock(BreakEvent event) {
        EntityPlayer player = event.getPlayer();
        if (player != null) {
            Block block = event.state.getBlock();
            int meta = block.getMetaFromState(event.state);
            if (CraftingEvents.isEventCancelled(player, ActionType.BREAKBLOCKWITH, player.getCurrentEquippedItem(), ActionType.BREAKBLOCK, BlockActionHelper.getStackFromBlockData(block, meta))) {
                event.setCanceled(true);
            }
        }
    }
}
