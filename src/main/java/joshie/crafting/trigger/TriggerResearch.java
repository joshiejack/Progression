package joshie.crafting.trigger;

import joshie.crafting.api.ITrigger;
import net.minecraft.nbt.NBTTagCompound;

import com.google.gson.JsonObject;

public class TriggerResearch extends TriggerBase {
	private String researchName;
	
	public TriggerResearch() {
		super("research");
	}
	
	@Override
	public ITrigger deserialize(JsonObject data) {
		TriggerResearch trigger = new TriggerResearch();
		trigger.researchName = data.get("Research Name").getAsString();
		return trigger;
	}

	@Override
	public void serialize(JsonObject data) {
		data.addProperty("Research Name", researchName);
	}
	
	public String getResearchName() {
		return researchName;
	}

	@Override
	public boolean isCompleted(Object[] existing) {
		return ((Boolean)existing[0]) == true;
	}

	@Override
	public Object[] onFired(Object[] existing, Object... data) {
		if (((String)data[0]).equals(researchName)) {
			return new Object[] { true };
		} else return new Object[] { false };
	}

	@Override
	public Object[] readFromNBT(NBTTagCompound tag) {
		return new Object[] { tag.getBoolean("HasResearch") };
	}

	@Override
	public void writeToNBT(NBTTagCompound tag, Object[] existing) {
		boolean completed = ((Boolean)existing[0]);
		tag.setBoolean("HasResearch", completed);
	}
}
