package joshie.progression.api.gui;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public interface IDrawHelper {
    public void drawText(String text, int left, int top, int color);

    public void drawSplitText(String text, int left, int top, int width, int color);

    public void drawRectangle(int left, int top, int width, int height, int color, int border);

    public void drawGradient(int left, int top, int width, int height, int color, int color2, int border);

    public void drawStack(ItemStack stack, int left, int top, float scale);

    public void drawText(int renderX, int renderY, String text, int left, int top, int color);

    public void drawSplitText(int renderX, int renderY, String text, int left, int top, int width, int color, float scale);

    public void drawGradient(int renderX, int renderY, int left, int top, int width, int height, int color, int color2, int border);

    public void drawStack(int renderX, int renderY, ItemStack stack, int left, int top, float scale);

    public void drawTexture(int renderX, int renderY, ResourceLocation resource, int left, int top, int u, int v, int width, int height);
}
