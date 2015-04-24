package joshie.crafting.trigger;

import java.util.UUID;

import joshie.crafting.api.ITriggerData;
import joshie.crafting.helpers.JSONHelper;
import joshie.crafting.trigger.data.DataCount;

import com.google.gson.JsonObject;

public abstract class TriggerBaseCounter extends TriggerBase {
    public int amount = 1;
    
	public TriggerBaseCounter(String name, int color) {
		super(name, color, "count");
	}

	@Override
	public boolean isCompleted(ITriggerData iTriggerData) {
		return ((DataCount)iTriggerData).count >= amount;
	}
	
	@Override
	public void onFired(UUID uuid, ITriggerData iTriggerData, Object... data) {	
		DataCount triggerData = (DataCount)iTriggerData;
		if (canIncrease(data)) {
			triggerData.count++;
		}
	}
	
	@Override
    public void readFromJSON(JsonObject data) {
	    amount = JSONHelper.getInteger(data, "amount", amount);
    }

    @Override
    public void writeToJSON(JsonObject data) {
        System.out.println(amount);
        JSONHelper.setInteger(data, "amount", amount, 1);
    }

	protected abstract boolean canIncrease(Object... data);
}
