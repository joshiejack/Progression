package joshie.crafting.json;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class DataReward extends DataGeneric {
	public DataReward() {}
	public DataReward(String type, String name, JsonObject data) {
		super(type, name, data);
	}
}
