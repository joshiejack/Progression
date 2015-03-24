package joshie.crafting.trigger;

import joshie.crafting.api.ITrigger;
import joshie.crafting.minetweaker.Triggers;
import minetweaker.MineTweakerAPI;
import net.minecraft.nbt.NBTTagCompound;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import com.google.gson.JsonObject;

@ZenClass("mods.craftcontrol.triggers.Research")
public class TriggerResearch extends TriggerBase {
	private String researchName;
	
	public TriggerResearch() {
		super("research");
	}
	
	@ZenMethod
	public void add(String unique, String name) {
		TriggerResearch trigger = new TriggerResearch();
		trigger.researchName = name;
		MineTweakerAPI.apply(new Triggers(unique, trigger));
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
		return asBoolean(existing) == true;
	}

	@Override
	public Object[] onFired(Object[] existing, Object... data) {
		if (asString(data).equals(researchName)) {
			return new Object[] { true };
		} else return new Object[] { false };
	}

	@Override
	public Object[] readFromNBT(NBTTagCompound tag) {
		return new Object[] { tag.getBoolean("HasResearch") };
	}

	@Override
	public void writeToNBT(NBTTagCompound tag, Object[] existing) {
		boolean completed = asBoolean(existing);
		tag.setBoolean("HasResearch", completed);
	}
}
