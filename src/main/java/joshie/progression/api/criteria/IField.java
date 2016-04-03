package joshie.progression.api.criteria;

import joshie.progression.gui.core.DrawHelper;

public interface IField {
    public String getFieldName();
    public void click();
    public boolean attemptClick(int mouseX, int mouseY);
    public void draw(DrawHelper helper, int renderX, int renderY, int color, int yPos, int mouseX, int mouseY);
    public String getField();
}
