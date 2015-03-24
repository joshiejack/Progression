package joshie.crafting.json;

import com.google.gson.JsonObject;

public class DataTrigger extends DataGeneric {
	String[] conditions;
	
	public DataTrigger() {}
	public DataTrigger(String type, String name, JsonObject data, String[] conditions) {
		super(type, name, data);
		this.conditions = conditions;
	}
}
