package joshie.crafting.json;

import java.util.List;

public class DataCriteria {
	public DataCriteria() {}
	public DataCriteria(String name, List<DataTrigger> triggers, List<DataGeneric> rewards, String[] requirements, String[] conflicts, int x, int y) {
		this.name = name;
		this.triggers = triggers;
		this.rewards = rewards;
		this.prereqs = requirements;
		this.conflicts = conflicts;
		this.x = x;
		this.y = y;
	}
	
	String name;
	List<DataTrigger> triggers;
	List<DataGeneric> rewards;
	///String[] triggers;
	///String[] rewards;
	String[] prereqs;
	String[] conflicts;
	int repeatable;
	int x;
	int y;
}
