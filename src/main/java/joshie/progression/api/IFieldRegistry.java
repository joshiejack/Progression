package joshie.progression.api;

import joshie.progression.api.criteria.IField;
import joshie.progression.api.criteria.IFieldProvider;

public interface IFieldRegistry {
    /** Returns an IField that will display lists of IFilter
     *  @param provider the provider, normally a reward, trigger, condition or filter
     *  @name the name of the field in your class
     *  @x    the xcoordinate to display the field
     *  @y    the ycoordinate to display the field
     *  @scale the scale to display the field **/
    public IField getItemPreview(IFieldProvider provider, String string, int x, int y, float scale);

    /** Returns an IField that will display a single item
     *  @param provider the provider, normally a reward, trigger, condition or filter
     *  @name the name of the field in your class
     *  @x    the xcoordinate to display the field
     *  @y    the ycoordinate to display the field
     *  @scale the scale to display the field **/
    public IField getItem(IFieldProvider provider, String string, int x, int y, float scale);
}
