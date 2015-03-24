package joshie.crafting.minetweaker;

import joshie.crafting.api.CraftingAPI;
import joshie.crafting.api.IReward;
import minetweaker.IUndoableAction;

import com.google.gson.JsonObject;

public class Rewards implements IUndoableAction {
	public String type;
	public String unique;
	public JsonObject data;

	public Rewards(String unique, IReward reward) {
		JsonObject object = new JsonObject();
		reward.serialize(object);
		this.type = reward.getTypeName();
		this.unique = unique;
		this.data = object;
	}

	@Override
	public void apply() {
		CraftingAPI.registry.getReward(type, unique, data);
	}

	@Override
	public boolean canUndo() {
		return true;
	}

	@Override
	public void undo() {
		CraftingAPI.registry.removeReward(unique);
	}

	@Override
	public String describe() {
		return "Adding the reward with the unique name of " + unique;
	}

	@Override
	public String describeUndo() {
		return "Removing the reward with the unique name of " + unique;
	}

	@Override
	public Object getOverrideKey() {
		return null;
	}
}
