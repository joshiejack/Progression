package joshie.progression.gui.base;

import joshie.progression.api.IDrawHelper;
import joshie.progression.gui.GuiCriteriaEditor;
import net.minecraft.item.ItemStack;

public class DrawHelper implements IDrawHelper {
    public static final DrawHelper INSTANCE = new DrawHelper();

    private int xPosition = 0;
    private int yPosition = 0;

    public void setOffset(int x, int y) {
        this.xPosition = x;
        this.yPosition = y;
    }

    @Override
    public void drawText(String text, int x, int y, int color) {
        GuiCriteriaEditor.INSTANCE.drawText(text, xPosition + x, y + yPosition, color);
    }

    @Override
    public void drawSplitText(String text, int x, int y, int width, int fontColor) {
        GuiCriteriaEditor.INSTANCE.drawSplitText(text, xPosition + x, y + yPosition, width, fontColor);
    }

    @Override
    public void drawGradient(int x, int y, int width, int height, int color, int color2, int border) {
        GuiCriteriaEditor.INSTANCE.drawGradient(xPosition + x, y + yPosition, width, height, color, color2, border);
    }

    public void drawBox(int x, int y, int width, int height, int color, int border) {
        GuiCriteriaEditor.INSTANCE.drawBox(xPosition + x, y + yPosition, width, height, color, border);
    }

    @Override
    public void drawStack(ItemStack stack, int x, int y, float scale) {
        GuiCriteriaEditor.INSTANCE.drawStack(stack, xPosition + x, y + yPosition, scale);
    }

    @Override
    public void drawTexture(int x, int y, int u, int v, int width, int height) {
        GuiCriteriaEditor.INSTANCE.drawTexture(xPosition + x, y + yPosition, u, v, width, height);
    }

    @Override
    public int getXPosition() {
        return xPosition;
    }
}