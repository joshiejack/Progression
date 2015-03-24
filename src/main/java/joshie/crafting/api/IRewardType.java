package joshie.crafting.api;

import com.google.gson.JsonObject;

public interface IRewardType {
	/** Returns the type name **/
	public String getTypeName();
	
	/** Creates an IReward from JSON **/
	public IReward deserialize(JsonObject data);
	
	/** Converts an IReward to JSON **/
	public void serialize(JsonObject elements);
}
