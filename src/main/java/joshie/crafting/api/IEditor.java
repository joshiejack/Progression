package joshie.crafting.api;

public interface IEditor {
    public boolean click(int mouseX, int mouseY, boolean isDoubleClick);
    
    public void draw(int x, int y, int offsetX);
}
