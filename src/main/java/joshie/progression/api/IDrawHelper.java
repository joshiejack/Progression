package joshie.progression.api;

import net.minecraft.item.ItemStack;

public interface IDrawHelper {
    /** Relative positioning **/
    public int getXPosition();

    /** Draws a gradient box **/
    public void drawGradient(int x, int y, int width, int height, int gradient1, int gradient2, int border);

    /** Draws text **/
    public void drawText(String text, int x, int y, int fontColor);
    
    /** Draws split text **/
    public void drawSplitText(String text, int x, int y, int width, int fontColor);

    /** Draws a textured rectangle **/
    public void drawTexture(int x, int y, int u, int v, int h, int w);

    /** Draws an item stack **/
    public void drawStack(ItemStack stack, int x, int y, float scale);
}