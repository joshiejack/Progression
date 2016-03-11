package joshie.progression.api;

import joshie.progression.gui.newversion.overlays.DrawFeatureHelper;

public interface IField {
    public void click();
    public boolean attemptClick(int mouseX, int mouseY);
    public void draw(DrawFeatureHelper helper, int renderX, int renderY, int color, int yPos);
    public void setObject(Object object);
    public String getField();
}
