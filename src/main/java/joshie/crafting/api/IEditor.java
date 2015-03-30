package joshie.crafting.api;

public interface IEditor {
    public boolean click(int mouseX, int mouseY, boolean isDoubleClick);
    
    /** returns true if this should be deleted **/
    public boolean keyTyped(char character, int key);
    public void draw(int x, int y, int offsetX);
}
