package joshie.progression.api;

import net.minecraft.item.ItemStack;

public class DrawHelper {
    public static IDrawHelper draw = null;

    public static interface IDrawHelper {
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
    
    public static void drawGradient(int x, int y, int width, int height, int gradient1, int gradient2, int border) {
        draw.drawGradient(x, y, width, height, gradient1, gradient2, border);
    }

    public static void drawText(String text, int x, int y, int fontColor) {
        draw.drawText(text, x, y, fontColor);
    }

    public static void drawSplitText(String text, int x, int y, int width, int fontColor) {
        draw.drawSplitText(text, x, y, width, fontColor);
    }

    public static void drawStack(ItemStack stack, int x, int y, float scale) {
        draw.drawStack(stack, x, y, scale);
    }
    
    public static void drawTexture(int x, int y, int u, int v, int h, int w) {
        draw.drawTexture(x, y, u, v, h, w);
    }
}
