package joshie.crafting.api;

import com.google.gson.JsonObject;

public interface IRewardType {
	/** Returns the type name **/
	public String getTypeName();
    
    /** Localized name **/
    public String getLocalisedName();
	
	/** Creates an IReward from JSON **/
	public IReward deserialize(JsonObject data);
	
	/** Creates an IReward with defaults **/
	public IReward newInstance();
	
	/** Converts an IReward to JSON **/
	public void serialize(JsonObject elements);

	public Bus getBusType();
}
