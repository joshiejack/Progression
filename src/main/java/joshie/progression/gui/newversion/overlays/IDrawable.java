package joshie.progression.gui.newversion.overlays;

import joshie.progression.api.IFieldProvider;

public interface IDrawable {
	/** Return the field provider **/
	public IFieldProvider getProvider();

	/** Called every 200 ticks to update displays **/
    public void update();

    /** Return the color of the bar **/
    public int getColor();
    
    /** Return the name of this drawable **/
    public String getLocalisedName();
    
    /** Return the unlocalised name of this drawable **/
    public String getUnlocalisedName();

    /** Return the description of this drawable
     *  Used in display mode */
    public String getDescription();
}
