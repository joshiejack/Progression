package joshie.progression.criteria.rewards;

import joshie.progression.api.ISpecialFilters;
import joshie.progression.crafting.ActionType;
import joshie.progression.gui.newversion.overlays.IFilterSelectorFilter;
import joshie.progression.gui.selector.filters.BlockFilter;
import joshie.progression.handlers.CraftingEvents;
import joshie.progression.helpers.BlockActionHelper;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class RewardBreakBlock extends RewardBaseAction implements ISpecialFilters {
    public RewardBreakBlock() {
        super("breakBlock", 0xFF74246D, ActionType.BREAKBLOCK);
    }

    @SubscribeEvent
    public void onBreakSpeed(BreakSpeed event) {
    	Block block = event.state.getBlock();
    	int meta = block.getMetaFromState(event.state);
        if (CraftingEvents.isEventCancelled(event.entityPlayer, ActionType.BREAKBLOCK, event.entityPlayer.getCurrentEquippedItem(), BlockActionHelper.getStackFromBlockData(block, meta))) {
            event.newSpeed = 0F;
        }
    }

    @SubscribeEvent
    public void onBreakBlock(BreakEvent event) {
        EntityPlayer player = event.getPlayer();
        if (player != null) {
        	Block block = event.state.getBlock();
        	int meta = block.getMetaFromState(event.state);
            if (CraftingEvents.isEventCancelled(player, ActionType.BREAKBLOCK, player.getCurrentEquippedItem(), BlockActionHelper.getStackFromBlockData(block, meta))) {
                event.setCanceled(true);
            }
        }
    }

    @Override
    public IFilterSelectorFilter getFilterForField(String fieldName) {
        return BlockFilter.INSTANCE;
    }
}
