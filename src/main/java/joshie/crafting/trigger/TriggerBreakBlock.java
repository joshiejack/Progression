package joshie.crafting.trigger;

import joshie.crafting.api.CraftingAPI;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class TriggerBreakBlock extends TriggerBaseBlock {
    public TriggerBreakBlock() {
        super("breakBlock", 0xFFCCCCCC);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onEvent(BreakEvent event) {
        CraftingAPI.registry.fireTrigger(event.getPlayer(), getUnlocalisedName(), event.block, event.blockMetadata);
    }
}
