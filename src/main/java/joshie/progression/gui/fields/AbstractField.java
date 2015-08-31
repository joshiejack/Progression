package joshie.progression.gui.fields;


public abstract class AbstractField {
    public String name;
    public Object object;

    public AbstractField(String name) {
        this.name = name;
    }

    public abstract void click();

    public abstract void draw(int color, int yPos);

    public boolean attemptClick(int mouseX, int mouseY) {
        return false;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
