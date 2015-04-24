package joshie.crafting.api;

import java.util.UUID;

import com.google.gson.JsonObject;

public interface ITriggerNew {
    /** Create a new blank instance of this trigger **/
    public ITriggerNew newInstance();
    
    /** Create a new instance for storing data **/
    public ITriggerData newData();
    
    /** Returns the name of this trigger type, used to call from the json **/
    public String getUniqueName();
    
    /** Returns the colour used for this trigger type **/
    public int getColor();

    /** This class will automatically be registered/de-registered from the event
     *  buses that are returned from this */
    public Bus[] getEventBusTypes();
    
    /** Called to fire this trigger **/
    public void onFired(UUID uuid, ITriggerData triggerData, Object... data);

    /** Called to load the data about this trigger from json **/
    public void readFromJSON(JsonObject object);
    
    /** Called to save the data about this trigger to json **/
    public void writeToJSON(JsonObject object);
}
