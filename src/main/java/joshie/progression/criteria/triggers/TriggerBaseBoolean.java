package joshie.progression.criteria.triggers;

import java.util.UUID;

import joshie.progression.api.ITriggerData;
import joshie.progression.criteria.triggers.data.DataBoolean;

public abstract class TriggerBaseBoolean extends TriggerBase {
    public TriggerBaseBoolean(String name, int color) {
        super(name, color, "boolean");
    }
	
	@Override
	public boolean isCompleted(ITriggerData iTriggerData) {
		return ((DataBoolean)iTriggerData).completed;
	}
	
	@Override
	public void onFired(UUID uuid, ITriggerData iTriggerData, Object... data) {	
		((DataBoolean)iTriggerData).completed = isTrue(data);
	}

	protected boolean isTrue(Object... data) {
	    return false;
	}
}