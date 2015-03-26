package joshie.crafting.api;

public interface IEditor {
    public void click(int mouseX, int mouseY, boolean isDoubleClick);
    public void release(int mouseX, int mouseY);
    public void follow(int mouseX, int mouseY);
    public void scroll(boolean b);
    public void keyTyped(char character, int key);
    public void draw(int x, int y, int offsetX);
}
