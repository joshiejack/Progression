package joshie.crafting.minetweaker;

import joshie.crafting.api.CraftingAPI;
import joshie.crafting.api.ICondition;
import minetweaker.IUndoableAction;

import com.google.gson.JsonObject;

public class Conditions implements IUndoableAction {
	public String type;
	public String unique;
	public JsonObject data;
	
	public Conditions(String unique, ICondition condition) {
		JsonObject object = new JsonObject();
		condition.serialize(object);
		this.type = condition.getTypeName();
		this.unique = unique;
		this.data = object;
	}
	
	@Override
	public void apply() {
		CraftingAPI.registry.getCondition(type, unique, data);
	}

	@Override
	public boolean canUndo() {
		return true;
	}

	@Override
	public void undo() {
		CraftingAPI.registry.removeCondition(unique);
	}

	@Override
	public String describe() {
		return "Adding the condition with the unique name of " + unique;
	}

	@Override
	public String describeUndo() {
		return "Removing the condition with the unique name of " + unique;
	}

	@Override
	public Object getOverrideKey() {
		return null;
	}
}
