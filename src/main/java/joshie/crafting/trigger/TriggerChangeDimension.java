package joshie.crafting.trigger;

import joshie.crafting.api.Bus;
import joshie.crafting.api.CraftingAPI;
import joshie.crafting.gui.fields.BooleanField;
import joshie.crafting.gui.fields.TextField;
import joshie.crafting.helpers.JSONHelper;

import com.google.gson.JsonObject;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;

public class TriggerChangeDimension extends TriggerBaseCounter {
    public int times = 1;
    public boolean checkFrom = false;
    public int from = 0;
    public boolean checkTo = true;
    public int to = -1;

    public TriggerChangeDimension() {
        super("changeDimension", 0xFF000000);
        list.add(new TextField("times", this));
        list.add(new BooleanField("checkFrom", this));
        list.add(new TextField("from", this));
        list.add(new BooleanField("checkTo", this));
        list.add(new TextField("to", this));
    }

    @Override
    public Bus getEventBus() {
        return Bus.FML;
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onEvent(PlayerChangedDimensionEvent event) {
        CraftingAPI.registry.fireTrigger(event.player, getUnlocalisedName(), event.fromDim, event.toDim);
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
