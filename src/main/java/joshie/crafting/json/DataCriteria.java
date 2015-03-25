package joshie.crafting.json;

import com.google.gson.annotations.SerializedName;

public class DataCriteria {
	public DataCriteria() {}
	public DataCriteria(String name, String[] triggers, String[] rewards, String[] requirements, String[] conflicts, int x, int y) {
		this.name = name;
		this.triggers = triggers;
		this.rewards = rewards;
		this.prereqs = requirements;
		this.conflicts = conflicts;
		this.x = x;
		this.y = y;
	}
	
	@SerializedName("Unique Name")
	String name;
	
	@SerializedName("Triggers")
	String[] triggers;
	
	@SerializedName("Rewards")
	String[] rewards;
	
	@SerializedName("Requirements")
	String[] prereqs;
	
	@SerializedName("Conflicts")
	String[] conflicts;
	
	@SerializedName("Repeatable Amount")
	int repeatable;
	
	@SerializedName("X Coordinate")
	int x;
	
	@SerializedName("Y Coordinate")
	int y;
}
