package joshie.crafting.api;

public interface ICriteriaEditor extends IEditor {
    public void drawText(String text, int x, int y, int color);
    public void drawBox(int x, int y, int width, int height, int color, int border);
}
