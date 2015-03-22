package joshie.crafting.json;

import com.google.gson.annotations.SerializedName;

public class DataGeneric {
	public DataGeneric() {}
	public DataGeneric(String type, String name, String data) {
		this.name = name;
		this.type = type;
		this.data = data;
	}
	
	@SerializedName("Unique Name")
	String name;
	
	@SerializedName("Type")
	String type;
	
	@SerializedName("Data")
	String data;
}
