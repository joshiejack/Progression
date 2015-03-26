package joshie.crafting.trigger;

import joshie.crafting.api.ITriggerData;
import joshie.crafting.trigger.data.DataBoolean;

public abstract class TriggerBaseBoolean extends TriggerBase {
	public TriggerBaseBoolean(String name) {
		super(name);
	}
	
	@Override
	public ITriggerData newData() {
		return new DataBoolean();
	}
	
	@Override
	public boolean isCompleted(ITriggerData iTriggerData) {
		return ((DataBoolean)iTriggerData).completed;
	}
	
	@Override
	public void onFired(ITriggerData iTriggerData, Object... data) {	
		((DataBoolean)iTriggerData).completed = isTrue(data);
	}

	protected boolean isTrue(Object... data) {
	    return false;
	}
}