package joshie.progression.criteria.triggers;

import joshie.progression.api.IHasEventBus;
import joshie.progression.api.ProgressionAPI;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;

public class TriggerChangeDimension extends TriggerBaseCounter implements IHasEventBus {
    public boolean checkFrom = false;
    public int from = 0;
    public boolean checkTo = true;
    public int to = -1;

    public TriggerChangeDimension() {
        super(new ItemStack(Blocks.portal), "changeDimension", 0xFF000000);
    }
    
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onEvent(PlayerChangedDimensionEvent event) {
        ProgressionAPI.registry.fireTrigger(event.player, getUnlocalisedName(), event.fromDim, event.toDim);
    }

    @Override
    protected boolean canIncrease(Object... data) {
        int fromDim = (Integer) data[0];
        int toDim = (Integer) data[1];
        if (checkFrom) {
            if (from != fromDim) return false;
        }

        if (checkTo) {
            if (to != toDim) return false;
        }

        return true;
    }
}
