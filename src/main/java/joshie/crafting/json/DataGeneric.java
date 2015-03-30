package joshie.crafting.json;

import com.google.gson.JsonObject;

public class DataGeneric {
	public DataGeneric() {}
	public DataGeneric(String type, JsonObject data) {
		this.type = type;
		this.data = data;
	}
	
    public String type;
	public JsonObject data;
}
