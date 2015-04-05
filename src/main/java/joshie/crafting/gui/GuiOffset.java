package joshie.crafting.gui;

import joshie.crafting.helpers.StackHelper;
import net.minecraft.item.ItemStack;

public abstract class GuiOffset extends GuiBase {    
    public void drawSplitText(String text, int xCoord, int yCoord, int width, int color) {
        mc.fontRenderer.drawSplitString(text, offsetX + xCoord, y + yCoord, width, color);
    }

    public void drawText(String text, int xCoord, int yCoord, int color) {
        mc.fontRenderer.drawString(text, offsetX + xCoord, y + yCoord, color);
    }

    public void drawBox(int xCoord, int yCoord, int width, int height, int color, int border) {
        GuiCriteriaEditor.INSTANCE.drawRectWithBorder(offsetX + xCoord, y + yCoord, offsetX + xCoord + width, y + yCoord + height, color, border);
    }

    public void drawGradient(int xCoord, int yCoord, int width, int height, int color, int color2, int border) {
        GuiCriteriaEditor.INSTANCE.drawGradientRectWithBorder(offsetX + xCoord, y + yCoord, offsetX + xCoord + width, y + yCoord + height, color, color2, border);
    }

    public void drawStack(ItemStack stack, int xCoord, int yCoord, float scale) {
        StackHelper.drawStack(stack, offsetX + xCoord, y + yCoord, scale);
    }

    public void drawTexture(int xCoord, int yCoord, int u, int v, int width, int height) {
        GuiCriteriaEditor.INSTANCE.drawTexturedModalRect(offsetX + xCoord, y + yCoord, u, v, width, height);
    }
}
