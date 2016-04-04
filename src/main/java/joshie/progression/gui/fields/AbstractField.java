package joshie.progression.gui.fields;

import joshie.progression.api.criteria.IField;

public abstract class AbstractField implements IField {
    public String name;
    public Object object;

    public AbstractField(String name) {
        this.name = name;
    }

    public boolean attemptClick(int mouseX, int mouseY) {
        return false;
    }

    public void click(int button) {
        click();
    }

    public void click() {}

    public void setObject(Object object) {
        this.object = object;
    }
}
