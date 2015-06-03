package joshie.progression.criteria.triggers;

import joshie.progression.api.ProgressionAPI;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class TriggerBreakBlock extends TriggerBaseBlock {
    public TriggerBreakBlock() {
        super("breakBlock", 0xFFCCCCCC);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onEvent(BreakEvent event) {
        if (ProgressionAPI.registry.fireTrigger(event.getPlayer(), getUnlocalisedName(), event.block, event.blockMetadata) == Result.DENY) {
            event.setCanceled(true);
        }
    }
}
