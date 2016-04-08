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

    public boolean click(int button) {
        return click();
    }

    public boolean click() {
        return true;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    @Override
    public String toString() {
        return (String) getField();
    }
}
