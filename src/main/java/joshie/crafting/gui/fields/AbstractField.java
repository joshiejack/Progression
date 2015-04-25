package joshie.crafting.gui.fields;


public abstract class AbstractField {
    public String name;

    public AbstractField(String name) {
        this.name = name;
    }

    public abstract void click();

    public abstract void draw(int color, int yPos);

    public boolean attemptClick(int mouseX, int mouseY) {
        return false;
    }

    public abstract void setObject(Object object);
}
