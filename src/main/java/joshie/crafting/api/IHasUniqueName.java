package joshie.crafting.api;

public interface IHasUniqueName {
	/** returns the unique name for this trigger **/
	public String getUniqueName();
	
	/** Sets the unique name **/
	public IHasUniqueName setUniqueName(String unique);
}
