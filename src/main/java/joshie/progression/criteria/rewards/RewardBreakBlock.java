package joshie.progression.criteria.rewards;

import joshie.progression.crafting.ActionType;
import joshie.progression.handlers.CraftingEvents;
import joshie.progression.helpers.BlockActionHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class RewardBreakBlock extends RewardBaseAction {
    public RewardBreakBlock() {
        super("breakBlock", 0xFF74246D);
        this.type = ActionType.BREAKBLOCK;
    }

    @SubscribeEvent
    public void onBreakSpeed(BreakSpeed event) {
        if (CraftingEvents.isEventCancelled(event.entityPlayer, ActionType.BREAKBLOCK, event.entityPlayer.getCurrentEquippedItem(), BlockActionHelper.getStackFromBlockData(event.block, event.metadata))) {
            event.newSpeed = 0F;
        }
    }

    @SubscribeEvent
    public void onBreakBlock(BreakEvent event) {
        EntityPlayer player = event.getPlayer();
        if (player != null) {
            if (CraftingEvents.isEventCancelled(player, ActionType.BREAKBLOCK, player.getCurrentEquippedItem(), BlockActionHelper.getStackFromBlockData(event.block, event.blockMetadata))) {
                event.setCanceled(true);
            }
        }
    }
}
