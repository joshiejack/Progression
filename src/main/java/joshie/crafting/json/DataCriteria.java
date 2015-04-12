package joshie.crafting.json;

import java.util.List;

public class DataCriteria {
	public DataCriteria() {}
	public DataCriteria(String uniqueName, String displayName, List<DataTrigger> triggers, List<DataGeneric> rewards, String[] requirements, String[] conflicts, int x, int y, boolean isVisible) {
		this.uniqueName = uniqueName;
		this.displayName = displayName;
		this.triggers = triggers;
		this.rewards = rewards;
		this.prereqs = requirements;
		this.conflicts = conflicts;
		this.x = x;
		this.y = y;
		this.isVisible = isVisible;
	}
	
	String uniqueName;
	String displayName;
    String displayStack;
	List<DataTrigger> triggers;
	List<DataGeneric> rewards;
	String[] prereqs;
	String[] conflicts;
	int repeatable;
	int x;
	int y;
	boolean isVisible;
}
