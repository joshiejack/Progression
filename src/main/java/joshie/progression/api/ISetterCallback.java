package joshie.progression.api;

/** Implement this on IFieldProviders that need to perform, 
 *  special actions when their fields are set,
 *  This is only ever called when a string field, an item field,
 *  iitemfilter, ientityfilter list are set. You should return true,
 *  if this field was set, return false if the default behaviour should occur */
public interface ISetterCallback {

    /** Set the field with the fieldname passed in **/
    public boolean setField(String fieldName, Object object);
}
