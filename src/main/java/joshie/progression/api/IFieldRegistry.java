package joshie.progression.api;

import joshie.progression.api.criteria.IField;

public interface IFieldRegistry {
    /** Returns an IField that will display lists of IFilter
     *  @param provider the provider, normally a reward, trigger, condition or filter
     *  @name the name of the field in your class
     *  @x    the xcoordinate to display the field
     *  @y    the ycoordinate to display the field
     *  @scale the scale to display the field **/
    public IField getItemPreview(Object provider, String string, int x, int y, float scale);

    /** Returns an IField that will display a single item
     *  @param provider the provider, normally a reward, trigger, condition or filter
     *  @name the name of the field in your class
     *  @x    the xcoordinate to display the field
     *  @y    the ycoordinate to display the field
     *  @scale the scale to display the field **/
    public IField getItem(Object provider, String string, int x, int y, float scale);

    /** Returns an item filter field **/
    public IField getFilter(Object provider, String name);

    /** Returns a boolean field **/
    public IField getBoolean(Object provider, String name);

    /** returns a field that will toggle between reading/writing a boolean and a string **/
    public IField getToggleBoolean(Object provider, String booleanName, String stringName);

    /** Returns a text field, this is used for integers, string, float and doubles **/
    public IField getText(Object criteria, String name);
}
