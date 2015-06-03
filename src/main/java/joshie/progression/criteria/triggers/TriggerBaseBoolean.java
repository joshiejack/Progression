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
	public boolean onFired(UUID uuid, ITriggerData iTriggerData, Object... data) {	
	    if (!super.onFired(uuid, iTriggerData, data)) return false;
		((DataBoolean)iTriggerData).completed = isTrue(data);
		return true;
	}

	protected boolean isTrue(Object... data) {
	    return false;
	}
}