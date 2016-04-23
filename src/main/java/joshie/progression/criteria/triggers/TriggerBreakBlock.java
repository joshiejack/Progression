package joshie.progression.criteria.triggers;

import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.criteria.ITrigger;
import joshie.progression.api.criteria.ProgressionRule;
import joshie.progression.api.special.IMiniIcon;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static joshie.progression.ItemProgression.ItemMeta.breaking;
import static joshie.progression.ItemProgression.getStackFromMeta;

@ProgressionRule(name = "breakBlock", color = 0xFFDDDDDD, cancelable = true)
public class TriggerBreakBlock extends TriggerBaseBlock implements IMiniIcon {
    private static final ItemStack mini = getStackFromMeta(breaking);

    @Override
    public ITrigger copy() {
        return copyCounter(copyFilter(new TriggerBreakBlock()));
    }

    @Override
    public ItemStack getMiniIcon() {
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
