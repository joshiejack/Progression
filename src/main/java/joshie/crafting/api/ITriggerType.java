package joshie.crafting.api;

import com.google.gson.JsonObject;


public interface ITriggerType {
	/** Returns the types name **/
	public String getTypeName();
	
	public Bus getBusType();
	
	/** Creates an ITrigger from JSON **/
	public ITrigger deserialize(JsonObject data);
	
	/** Converts an ITrigger to JSON **/
	public void serialize(JsonObject elements);
}
