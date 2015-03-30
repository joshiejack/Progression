package joshie.crafting.api;

import com.google.gson.JsonObject;


public interface ITriggerType {
	/** Returns the types name **/
	public String getTypeName();
	
	/** Returns the localised name **/
    public String getLocalisedName();
	
	public Bus getBusType();
	
	/** Creates an ITrigger from JSON **/
	public ITrigger deserialize(JsonObject data);

	/** Creates a default trigger **/
    public ITrigger newInstance();
	
	/** Converts an ITrigger to JSON **/
	public void serialize(JsonObject elements);
}
