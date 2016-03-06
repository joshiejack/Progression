package joshie.progression.criteria.triggers;

import com.google.gson.JsonObject;

import joshie.progression.api.EventBusType;
import joshie.progression.gui.fields.TextField;
import joshie.progression.helpers.JSONHelper;

public class TriggerResearch extends TriggerBaseBoolean {
    public String name = "dummy";

    public TriggerResearch() {
        super("research", 0xFF26C9FF);
        list.add(new TextField("name", this));
    }

    @Override
    public EventBusType getEventBus() {
        return EventBusType.NONE;
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
