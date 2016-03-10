package joshie.progression.gui.newversion.overlays;

import java.util.List;

import net.minecraftforge.fml.common.eventhandler.Event.Result;

public interface IDrawable {
    /** Draw shit with the assistance of the helper **/
	public void draw(DrawFeatureHelper draw, int offsetX, int offsetY, int mouseOffsetX, int mouseOffsetY);
	
	/** Return true if a mouse click was processed */
	public boolean mouseClicked(int mouseOffsetX, int mouseOffsetY, int xPos, int button);

	/** Remove this thingy from the list **/
	public void remove(List list);
}
