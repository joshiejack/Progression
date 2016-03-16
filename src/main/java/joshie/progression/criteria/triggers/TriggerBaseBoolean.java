package joshie.progression.criteria.triggers;

import java.util.UUID;

import joshie.progression.api.EventBusType;
import joshie.progression.api.ITriggerData;
import joshie.progression.criteria.triggers.data.DataBoolean;

public abstract class TriggerBaseBoolean extends TriggerBase {
    private EventBusType[] events;

    public TriggerBaseBoolean(String name, int color, EventBusType... types) {
        super(name, color, "boolean");
        events = (types == null || types.length == 0) ? new EventBusType[] { EventBusType.FORGE } : types;
    }

    @Override
    public EventBusType[] getEventBusTypes() {
        return events;
    }

    @Override
    public boolean isCompleted(ITriggerData iTriggerData) {
        return ((DataBoolean) iTriggerData).completed;
    }

    @Override
    public boolean onFired(UUID uuid, ITriggerData iTriggerData, Object... data) {
        ((DataBoolean) iTriggerData).completed = isTrue(data);
        return true;
    }

    protected boolean isTrue(Object... data) {
        return false;
    }

    protected void markTrue(ITriggerData iTriggerData) {
        ((DataBoolean) iTriggerData).completed = true;
    }
}