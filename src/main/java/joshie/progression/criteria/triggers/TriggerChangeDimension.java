package joshie.progression.criteria.triggers;

import joshie.progression.Progression;
import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.criteria.ITrigger;
import joshie.progression.api.criteria.ProgressionRule;
import joshie.progression.api.special.DisplayMode;
import joshie.progression.api.special.ICustomDescription;
import joshie.progression.api.special.ICustomWidth;
import joshie.progression.helpers.DimensionHelper;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;

@ProgressionRule(name="changeDimension", color=0xFF4C0000, meta="onChangeDimension")
public class TriggerChangeDimension extends TriggerBaseCounter implements ICustomDescription, ICustomWidth {
    public boolean checkFrom = false;
    public int from = 0;
    public boolean checkTo = true;
    public int to = -1;

    @Override
    public ITrigger copy() {
        TriggerChangeDimension trigger = new TriggerChangeDimension();
        trigger.checkFrom = checkFrom;
        trigger.from = from;
        trigger.checkTo = checkTo;
        trigger.to = to;
        return copyCounter(trigger);
    }

    @Override
    public String getDescription() {
        StringBuilder builder = new StringBuilder();
        if (checkTo) builder.append(Progression.translate("trigger.changeDimension.go") + " " + DimensionHelper.getDimensionNameFromID(to));
        if (checkFrom && checkTo) builder.append("\n");
        if (checkFrom) builder.append(Progression.translate("trigger.changeDimension.leave")+ " " + DimensionHelper.getDimensionNameFromID(from));
        if (!checkTo && !checkFrom) builder.append(Progression.translate("trigger.changeDimension.change"));
        return  builder.toString();
    }

    @Override
    public int getWidth(DisplayMode mode) {
        return mode == DisplayMode.EDIT ? 100 : 80;
    }
    
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onEvent(PlayerChangedDimensionEvent event) {
        ProgressionAPI.registry.fireTrigger(event.player, getProvider().getUnlocalisedName(), event.fromDim, event.toDim);
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
