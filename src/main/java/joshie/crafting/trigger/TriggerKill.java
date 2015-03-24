package joshie.crafting.trigger;

import joshie.crafting.api.ITrigger;
import net.minecraft.nbt.NBTTagCompound;

import com.google.gson.JsonObject;

public class TriggerKill extends TriggerBase {
	private String entity;
	private int amount = 1;
	
	public TriggerKill() {
		super("kill");
	}
	
	@Override
	public ITrigger deserialize(JsonObject data) {
		TriggerKill trigger = new TriggerKill();
		trigger.entity = data.get("Entity").getAsString();
		if (data.get("amount") != null) {
			trigger.amount = data.get("Amount").getAsInt();
		}
		
		return trigger;
	}
	
	@Override
	public void serialize(JsonObject data) {
		data.addProperty("Entity", entity);
		if (amount != 1) {
			data.addProperty("Amount", amount);
		}
	}

	@Override
	public boolean isCompleted(Object[] existing) {
		int killCount = (Integer) existing[0];
		return killCount >= amount;
	}

	@Override
	public Object[] onFired(Object[] existing, Object... data) {		
		int killCount = 0;
		if (existing != null) {
			killCount = ((Integer)existing[0]);
		}
		
		if (((String)data[0]).equals(entity)) {
			killCount++;
		}
		
		return new Object[] { killCount };
	}

	@Override
	public Object[] readFromNBT(NBTTagCompound tag) {
		return new Object[] { tag.getInteger("KillCount") };
	}

	@Override
	public void writeToNBT(NBTTagCompound tag, Object[] existing) {
		int killCount = (Integer) existing[0];
		tag.setInteger("KillCount", killCount);
	}
}
