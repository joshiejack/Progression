package joshie.crafting.api;

import net.minecraft.item.ItemStack;

public class DrawHelper {
    public static IDrawHelper triggerDraw = null;
    public static IDrawHelper rewardDraw = null;
    
    public static interface IDrawHelper {
        /** Relative positioning **/
        public int getXPosition();
        
        /** Draws a gradient box **/
        public void drawGradient(int x, int y, int width, int height, int gradient1, int gradient2, int border);

        /** Draws text **/
        public void drawText(String text, int x, int y, int fontColor);

        /** Draws a textured rectangle **/
        public void drawTexture(int x, int y, int u, int v, int h, int w);

        /** Draws an item stack **/
        public void drawStack(ItemStack stack, int x, int y, float scale);
    }
}
