package joshie.progression.api;

import com.google.gson.JsonObject;

import joshie.progression.gui.newversion.overlays.IDrawable;

public interface IFilter extends IDrawable, IFieldProvider {
    /** Read data about this filter from json **/
    public void readFromJSON(JsonObject data);
    
    /** Write data about htis filter to json **/
    public void writeToJSON(JsonObject typeData);

    /** Type name **/
    public String getUnlocalisedName();
    
    /** Return the localised name of this filter **/
    public String getLocalisedName();
}
