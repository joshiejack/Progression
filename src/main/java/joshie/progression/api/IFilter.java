package joshie.progression.api;

import java.util.List;

import joshie.progression.api.fields.IFieldProvider;

public interface IFilter extends IFieldProvider {
    /** Return true if the pass in object, matches this filter.
     *  Keep in mind this can pass in entities, itemstack,
     *  Lists or anything really, so make sure to validate
     *  all objects */
    public boolean matches(Object object);
    
    /** Returns a list of all things that match this filter
     *  May pass in something, or it can be null **/
    public List getMatches(Object object);
    
    /** Returns the type of filter this is **/
    public FilterType getType();
    
    public static enum FilterType {
        ITEM, BLOCK, POTIONEFFECT, ENTITY, LOCATION, CRAFTING;
    }
}
