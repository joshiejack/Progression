package joshie.progression.gui.core;

import net.minecraft.item.ItemStack;

import static joshie.progression.gui.core.GuiList.CORE;

public class DrawHelperOffset extends DrawHelper {   
	@Override
    public void drawText(String text, int left, int top, int color) {
        CORE.drawText(text, CORE.getOffsetX() + left, top, color);
    }

    @Override
    public void drawRectangle(int left, int top, int width, int height, int color, int border) {
        CORE.drawRectWithBorder(CORE.getOffsetX() + left, top, CORE.getOffsetX() + left + width, top + height, color, border);
    }
    
    @Override
    public void drawGradient(int left, int top, int width, int height, int color, int color2, int border) {
        CORE.drawGradientRectWithBorder(CORE.getOffsetX() + left, top, CORE.getOffsetX() + left + width, top + height, color, color2, border);
    }
    
    @Override
    public void drawStack(ItemStack stack, int left, int top, float scale) {
        CORE.drawStack(stack, CORE.getOffsetX() + left, top, scale);
    }
}
