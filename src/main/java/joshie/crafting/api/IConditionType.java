package joshie.crafting.api;

import com.google.gson.JsonObject;

public interface IConditionType {
	/** Returns the type name **/
	public String getTypeName();
	
	/** Creates an IReward from JSON **/
	public ICondition deserialize(JsonObject data);
	
	/** Converts an IReward to JSON **/
	public void serialize(JsonObject elements);

    public String getLocalisedName();

    public ICondition newInstance();
}
