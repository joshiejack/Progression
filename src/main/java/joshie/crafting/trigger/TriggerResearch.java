package joshie.crafting.trigger;

import joshie.crafting.api.Bus;
import joshie.crafting.api.IResearch;
import joshie.crafting.api.ITrigger;
import joshie.crafting.plugins.minetweaker.Triggers;
import minetweaker.MineTweakerAPI;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import com.google.gson.JsonObject;

@ZenClass("mods.craftcontrol.triggers.Research")
public class TriggerResearch extends TriggerBaseBoolean implements IResearch {
	private String researchName;
	
	public TriggerResearch() {
		super("research");
	}
	
	@Override
	public Bus getBusType() {
		return Bus.NONE;
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
		trigger.researchName = data.get("researchName").getAsString();
		return trigger;
	}

	@Override
	public void serialize(JsonObject data) {
		data.addProperty("researchName", researchName);
	}
	
	@Override	
	public String getResearchName() {
		return researchName;
	}

	@Override
	protected boolean isTrue(Object... data) {
		return asString(data).equals(researchName);
	}
}
