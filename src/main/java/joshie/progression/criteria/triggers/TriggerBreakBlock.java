package joshie.progression.criteria.triggers;

import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.criteria.ITrigger;
import joshie.progression.api.criteria.ProgressionRule;
import joshie.progression.api.special.IMiniIcon;
import joshie.progression.lib.ProgressionInfo;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@ProgressionRule(name = "breakBlock", color = 0xFFDDDDDD, cancelable = true)
public class TriggerBreakBlock extends TriggerBaseBlock implements IMiniIcon {
    private static final ResourceLocation mini = new ResourceLocation(ProgressionInfo.BOOKPATH + "break.png");
    @Override
    public ITrigger copy() {
        return copyCounter(copyFilter(new TriggerBreakBlock()));
    }

    @Override
    public ResourceLocation getMiniIcon() {
        return mini;
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onEvent(BreakEvent event) {
        Block block = event.state.getBlock();
        int meta = block.getMetaFromState(event.state);
        if (ProgressionAPI.registry.fireTrigger(event.getPlayer(), getProvider().getUnlocalisedName(), block, meta) == Result.DENY) {
            event.setCanceled(true);
        }
    }
}
