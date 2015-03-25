package joshie.crafting.json;

import com.google.gson.JsonObject;

public class DataReward extends DataGeneric {
	public DataReward() {}
	public DataReward(String type, String name, JsonObject data) {
		super(type, name, data);
	}
}
