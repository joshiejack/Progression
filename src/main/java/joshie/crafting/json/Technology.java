package joshie.crafting.json;

import java.util.HashSet;
import java.util.Set;

import joshie.crafting.api.CraftingAPI;
import joshie.crafting.api.tech.ITechnology;

import com.google.gson.annotations.SerializedName;

public class Technology implements ITechnology {
	@SerializedName("Prerequisites")
	Set<String> prereqs = new HashSet();
	@SerializedName("Incompatible")
	Set<String> incompatible = new HashSet();
	@SerializedName("Requirement Name")
	String name;
	@SerializedName("X Coordinate")
	int x;
	@SerializedName("Y Coordinate")
	int y;
	@SerializedName("Is Hidden")
	boolean isHidden;
	
	private transient Set<ITechnology> prerequisites;
	private transient Set<ITechnology> conflicts;
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public Set<ITechnology> getPrereqs() {
		if (prerequisites != null) return prerequisites;
		else {
			prerequisites = new HashSet();
			for (String s: prereqs) {
				prerequisites.add(CraftingAPI.tech.getTechnologyFromName(s));
			}
			
			return prerequisites;
		}
	}

	@Override
	public Set<ITechnology> getConflicts() {
		if (conflicts != null) return conflicts;
		else {
			conflicts = new HashSet();
			for (String s: incompatible) {
				conflicts.add(CraftingAPI.tech.getTechnologyFromName(s));
			}
			
			return conflicts;
		}
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Technology other = (Technology) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
