package joshie.progression.api;

import com.google.gson.JsonObject;

/** Implement this interface on rewards, triggers, conditions or filters 
 *  to have special cased loading and saving of JSON data,
 *  public variables will still be loaded by default.
 *  private variables and transient are totally ignored
 *  Will only read in booleans, integers, floats and Strings */
public interface ISpecialJSON {   
    /** If this is true, default reading will not be performed,
     *  and only these methods will be used to read / write extra data.*/
    public boolean onlySpecial();
    
    /** Read data from JSON **/
    public void readFromJSON(JsonObject data);
    
    /** Write data to JSON **/
    public void writeToJSON(JsonObject object);
}
