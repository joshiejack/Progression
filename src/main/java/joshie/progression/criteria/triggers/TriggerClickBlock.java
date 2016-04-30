package joshie.progression.criteria.triggers;

import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.criteria.ITrigger;
import joshie.progression.api.criteria.ProgressionRule;
import joshie.progression.api.special.IMiniIcon;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static joshie.progression.ItemProgression.ItemMeta.click;
import static joshie.progression.ItemProgression.getStackFromMeta;

@ProgressionRule(name = "clickBlock", color = 0xFF69008C, cancelable = true)
public class TriggerClickBlock extends TriggerBaseBlock implements IMiniIcon {
    private static final ItemStack mini = getStackFromMeta(click);

    @Override
    public ITrigger copy() {
        return copyCounter(copyFilter(new TriggerClickBlock()));
    }

    @Override
    public ItemStack getMiniIcon() {
        return mini;
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onEvent(PlayerInteractEvent event) {
        if (event.getPos() != null) {
            IBlockState state = event.getWorld().getBlockState(event.getPos());
            Block block = state.getBlock();
            int meta = block.getMetaFromState(state);
    
            if (ProgressionAPI.registry.fireTrigger(event.getEntityPlayer(), getProvider().getUnlocalisedName(), block, meta) == Result.DENY) {
                event.setCanceled(true);
            }
        }
    }
}
