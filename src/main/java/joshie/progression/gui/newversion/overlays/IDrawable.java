package joshie.progression.gui.newversion.overlays;

import java.util.List;

import joshie.progression.api.IField;

public interface IDrawable {
	/** Remove this thingy from the list **/
	public void remove(List list);
	
	/** Returns a list of IField **/
	public List<IField> getFields();

	/** Called every 200 ticks to update displays **/
    public void update();

    /** Return the color of the bar **/
    public int getColor();
    
    /** Return gradient1 **/
    public int getGradient1();
    
    /** Return gradient 2 **/
    public int getGradient2();
    
    /** Return font color **/
    public int getFontColor();

    /** Return the name of this drawable **/
    public String getLocalisedName();

    /** Return the description of this drawable
     *  Used in display mode */
    public String getDescription();

    /** Draw this drawble in display mode **/
    public void drawDisplay(int mouseX, int mouseY);
}
