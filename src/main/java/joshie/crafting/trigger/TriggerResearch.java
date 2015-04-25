package joshie.crafting.trigger;

import joshie.crafting.api.Bus;
import joshie.crafting.gui.fields.TextField;
import joshie.crafting.helpers.JSONHelper;

import com.google.gson.JsonObject;

public class TriggerResearch extends TriggerBaseBoolean {
    public String name = "dummy";

    public TriggerResearch() {
        super("research", 0xFF26C9FF);
        list.add(new TextField("name", this));
    }

    @Override
    public Bus getEventBus() {
        return Bus.NONE;
    }

    @Override
    public void readFromJSON (JsonObject data) {
        name = JSONHelper.getString(data, "researchName", name);
    }

    @Override
    public void writeToJSON(JsonObject data) {
        JSONHelper.setString(data, "researchName", name, "dummy");
    }

    @Override
    protected boolean isTrue(Object... data) {
        return ((String)data[0]).equals(name);
    }
}
