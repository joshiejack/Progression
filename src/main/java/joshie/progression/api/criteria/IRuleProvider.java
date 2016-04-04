package joshie.progression.api.criteria;

import joshie.progression.api.special.DisplayMode;

public interface IRuleProvider<T extends IRule> extends IUnique {
    /** Returns the underlying object this is providing */
    public T getProvided();

    /** Return an unlocalised name **/
    public String getUnlocalisedName();

    /** Return a description of this thingy,
     *  this is to be displayed in display mode */
    public String getDescription();

    /** Called in draw mode to draw the width **/
    public int getWidth(DisplayMode mode);

    /** Return a colour to display this thingy as **/
    public int getColor();
}
