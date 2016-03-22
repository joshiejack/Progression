package joshie.progression.gui.core;

import joshie.progression.json.Theme;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class DrawHelper {
    protected GuiCore guiDraw;
    protected int offsetX; //OffsetX on the scroll position

    public DrawHelper(GuiCore core) {
        guiDraw = core;
    }

    public GuiCore getGui() {
        return guiDraw;
    }

    public Theme getTheme() {
        return guiDraw.getTheme();
    }

    public void configure() {
        offsetX = guiDraw.offsetX; //Scroll position guiDraw.ySize; // Set that gui height
    }

    public void drawText(String text, int left, int top, int color) {
        guiDraw.drawText(text, left, top, color);
    }

    public void drawSplitText(String text, int left, int top, int width, int color) {
        guiDraw.drawSplitText(text, left, top, width, color);
    }

    public void drawRectangle(int left, int top, int width, int height, int color, int border) {
        guiDraw.drawRectWithBorder(left, top, left + width, top + height, color, border);
    }

    public void drawGradient(int left, int top, int width, int height, int color, int color2, int border) {
        guiDraw.drawGradientRectWithBorder(left, top, left + width, top + height, color, color2, border);
    }

    public void drawStack(ItemStack stack, int left, int top, float scale) {
        guiDraw.drawStack(stack, left, top, scale);
    }

    public void drawText(int renderX, int renderY, String text, int left, int top, int color) {
        guiDraw.drawText(text, offsetX + left + renderX, top + renderY, color);
    }

    public void drawSplitText(int renderX, int renderY, String text, int left, int top, int width, int color, float scale) {
        guiDraw.drawSplitText(text, offsetX + left + renderX, top + renderY, width, color, scale);
    }

    public void drawGradient(int renderX, int renderY, int left, int top, int width, int height, int color, int color2, int border) {
        guiDraw.drawGradientRectWithBorder(offsetX + left + renderX, top + renderY, offsetX + left + renderX + width, top + renderY + height, color, color2, border);
    }

    public void drawStack(int renderX, int renderY, ItemStack stack, int left, int top, float scale) {
        guiDraw.drawStack(stack, offsetX + left + renderX, top + renderY, scale);
    }

    public void drawTexture(int renderX, int renderY, ResourceLocation resource, int left, int top, int u, int v, int width, int height) {
        guiDraw.drawTexture(resource, offsetX + left + renderX, top + renderY, u, v, width, height);
    }
}
