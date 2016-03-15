package joshie.progression.criteria.triggers;

import joshie.progression.api.EventBusType;

public class TriggerResearch extends TriggerBaseBoolean {
    public String name = "dummy";

    public TriggerResearch() {
        super("research", 0xFF26C9FF);
    }

    @Override
    public EventBusType getEventBus() {
        return EventBusType.NONE;
    }

    @Override
    protected boolean isTrue(Object... data) {
        return ((String)data[0]).equals(name);
    }
}
