package joshie.crafting.minetweaker;

import joshie.crafting.api.CraftingAPI;
import joshie.crafting.api.ITrigger;
import minetweaker.IUndoableAction;

import com.google.gson.JsonObject;

public class Triggers implements IUndoableAction {
	public String type;
	public String unique;
	public JsonObject data;

	public Triggers(String unique, ITrigger trigger) {
		JsonObject object = new JsonObject();
		trigger.serialize(object);
		this.type = trigger.getTypeName();
		this.unique = unique;
		this.data = object;
	}

	@Override
	public void apply() {
		CraftingAPI.registry.getTrigger(type, unique, data);
	}

	@Override
	public boolean canUndo() {
		return true;
	}

	@Override
	public void undo() {
		CraftingAPI.registry.removeTrigger(unique);
	}

	@Override
	public String describe() {
		return "Adding the trigger with the unique name of " + unique;
	}

	@Override
	public String describeUndo() {
		return "Removing the trigger with the unique name of " + unique;
	}

	@Override
	public Object getOverrideKey() {
		return null;
	}
}
