package joshie.progression.criteria.triggers;

import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.criteria.ITrigger;
import joshie.progression.api.special.IHasEventBus;
import net.minecraft.block.Block;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TriggerBreakBlock extends TriggerBaseBlock implements IHasEventBus {
    public TriggerBreakBlock() {
        super("breakBlock", 0xFFCCCCCC);
    }

    @Override
    public ITrigger copy() {
        TriggerBreakBlock trigger = new TriggerBreakBlock();
        trigger.cancel = cancel;
        return copyBase(copyCounter(copyFilter(trigger)));
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onEvent(BreakEvent event) {
        Block block = event.state.getBlock();
        int meta = block.getMetaFromState(event.state);
        if (ProgressionAPI.registry.fireTrigger(event.getPlayer(), getUnlocalisedName(), block, meta) == Result.DENY) {
            event.setCanceled(true);
        }
    }
}
