package joshie.crafting.api;

public interface ITreeEditor extends IEditor {
    public int getX();
    public int getY();
    public void setCoordinates(int x, int y);
    void draw(int x, int y, int offsetX, int highlight);
    public void release(int mouseX, int mouseY);
    public void follow(int mouseX, int mouseY);
    public void scroll(boolean b);
    public boolean keyTyped(char character, int key);
    boolean isCriteriaVisible();
}
