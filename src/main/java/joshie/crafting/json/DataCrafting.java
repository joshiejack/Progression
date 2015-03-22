package joshie.crafting.json;

import com.google.gson.annotations.SerializedName;

public class DataCrafting {
	public DataCrafting() {}
	public DataCrafting(String type, String item, boolean matchDamage, boolean matchNBT, String... conditions) {
		this.type = type;
		this.item = item;
		this.damage = matchDamage;
		this.nbt = matchNBT;
		this.condition = conditions;
	}
	
	@SerializedName("Crafting Type")
	String type;
	
	@SerializedName("Condition Names")
	String[] condition;
	
	@SerializedName("Item")
	String item;
	
	@SerializedName("Match Damage")
	boolean damage;
	
	@SerializedName("Match NBT")
	boolean nbt;
}
