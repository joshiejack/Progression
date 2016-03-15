package joshie.progression.api.gui;

import joshie.progression.gui.newversion.overlays.DrawFeatureHelper;

/** Implement this on rewards, triggers, filters, conditions, 
 *  if you wish to draw something special on them, other than default fields. */
public interface ICustomDrawGuiDisplay {
    public void drawDisplay(DrawFeatureHelper helper, int renderX, int renderY, int mouseX, int mouseY);
}
