package joshie.crafting.api.tech;

import java.util.Set;

public interface ITechnology {
	/** @return			the name of the technology **/
	public String getName();

	/** @return 		returns the prereqs of this tech */
	public Set<ITechnology> getPrereqs();
	
	/** @return 		returns the techs that this tech conflicts with */
	public Set<ITechnology> getConflicts();
}
