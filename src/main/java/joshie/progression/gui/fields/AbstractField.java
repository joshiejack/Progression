package joshie.progression.gui.fields;

import joshie.progression.api.criteria.IProgressionField;

public abstract class AbstractField implements IProgressionField {
    public String name;
    public Object object;

    public AbstractField(String name) {
        this.name = name;
    }

    public boolean attemptClick(int mouseX, int mouseY) {
        return false;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
