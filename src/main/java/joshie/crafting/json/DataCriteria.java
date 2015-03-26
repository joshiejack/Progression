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
	
	String name;
	String[] triggers;
	String[] rewards;
	String[] prereqs;
	String[] conflicts;
	int repeatable;
	int x;
	int y;
}
