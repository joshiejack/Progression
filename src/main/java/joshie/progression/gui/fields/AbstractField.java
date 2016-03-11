package joshie.progression.gui.fields;

import joshie.progression.api.IField;
import joshie.progression.gui.newversion.overlays.DrawFeatureHelper;

public abstract class AbstractField implements IField {
    public String name;
    public Object object;

    public AbstractField(String name) {
        this.name = name;
    }

    public abstract void click();

    public abstract void draw(DrawFeatureHelper helper, int renderX, int renderY, int color, int yPos);

    public boolean attemptClick(int mouseX, int mouseY) {
        return false;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
