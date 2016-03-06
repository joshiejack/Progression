package joshie.progression.criteria.triggers;

import com.google.gson.JsonObject;

import joshie.progression.api.EventBusType;
import joshie.progression.api.ProgressionAPI;
import joshie.progression.gui.fields.BooleanField;
import joshie.progression.gui.fields.TextField;
import joshie.progression.helpers.JSONHelper;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;

public class TriggerChangeDimension extends TriggerBaseCounter {
    public boolean checkFrom = false;
    public int from = 0;
    public boolean checkTo = true;
    public int to = -1;

    public TriggerChangeDimension() {
        super("changeDimension", 0xFF000000);
        list.add(new BooleanField("checkFrom", this));
        list.add(new TextField("from", this));
        list.add(new BooleanField("checkTo", this));
        list.add(new TextField("to", this));
    }

    @Override
    public EventBusType getEventBus() {
        return EventBusType.FML;
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onEvent(PlayerChangedDimensionEvent event) {
        ProgressionAPI.registry.fireTrigger(event.player, getUnlocalisedName(), event.fromDim, event.toDim);
    }

    @Override
    public void readFromJSON(JsonObject data) {
        super.readFromJSON(data);
        checkFrom = JSONHelper.getExists(data, "from");
        from = JSONHelper.getInteger(data, "from", from);
        checkTo = JSONHelper.getExists(data, "to");
        to = JSONHelper.getInteger(data, "to", to);
    }

    @Override
    public void writeToJSON(JsonObject data) {
        super.writeToJSON(data);
        if (checkFrom) JSONHelper.setInteger(data, "from", from, -5000);
        if (checkTo) JSONHelper.setInteger(data, "to", to, -5000);
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
