package joshie.crafting.api;

import java.util.UUID;

public interface IResearch extends IHasUniqueName {
	/** Triggers the completion of research 
	 * @return **/
	public boolean complete(UUID uuid);
}
