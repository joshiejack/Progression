package joshie.crafting.json;

import com.google.gson.annotations.SerializedName;

public class Conditions {
	@SerializedName("Item")
	String item;
	@SerializedName("Requirement Type")
	String reqType;
	@SerializedName("Requirement Data")
	String reqData;
	@SerializedName("Crafting Type")
	String craftType;
}
