package joshie.crafting.json;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class DataCondition extends DataGeneric {
	public DataCondition() {}
	public DataCondition(String type, String name, JsonObject data) {
		super(type, name, data);
	}
}
