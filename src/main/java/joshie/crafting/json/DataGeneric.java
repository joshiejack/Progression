package joshie.crafting.json;

import com.google.gson.JsonObject;

public abstract class DataGeneric {
	public DataGeneric() {}
	public DataGeneric(String type, String name, JsonObject data) {
		this.name = name;
		this.type = type;
		this.data = data;
	}
	
	public JsonObject data;
	
	public String name, type;
}
