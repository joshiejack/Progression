package joshie.progression.api.fields;

import joshie.progression.criteria.triggers.TriggerClickBlock;

public interface IFieldRegistry {
    /** Returns an IField that will display lists of IFilter
     *  @param provider the provider, normally a reward, trigger, condition or filter
     *  @name the name of the field in your class
     *  @x    the xcoordinate to display the field
     *  @y    the ycoordinate to display the field
     *  @scale the scale to display the field **/
    public IField getItemPreview(IFieldProvider provider, String string, int x, int y, float scale);
}
