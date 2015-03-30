package joshie.crafting.trigger;

import joshie.crafting.api.ITriggerData;
import joshie.crafting.trigger.data.DataCount;

public abstract class TriggerBaseCounter extends TriggerBase {
	public TriggerBaseCounter(String localised, int color, String name) {
		super(localised, color, name);
	}
	
	@Override
	public ITriggerData newData() {
		return new DataCount();
	}
	
	public abstract int getAmountRequired();
	
	@Override
	public boolean isCompleted(ITriggerData iTriggerData) {
		return ((DataCount)iTriggerData).count >= getAmountRequired();
	}
	
	@Override
	public void onFired(ITriggerData iTriggerData, Object... data) {	
		DataCount triggerData = (DataCount)iTriggerData;
		if (canIncrease(data)) {
			triggerData.count++;
		}
	}

	protected abstract boolean canIncrease(Object... data);
}
