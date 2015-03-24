package joshie.crafting.json;

import com.google.gson.JsonObject;

public class DataTrigger extends DataGeneric {
	public DataTrigger() {}
	public DataTrigger(String type, String name, JsonObject data) {
		super(type, name, data);
	}
}
