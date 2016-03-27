package joshie.progression.gui.core;

import net.minecraft.item.ItemStack;

public class DrawHelperOffset extends DrawHelper {   
	public DrawHelperOffset(GuiCore core) {
		super(core);
	}

	@Override
    public void drawText(String text, int left, int top, int color) {
        guiDraw.drawText(text, offsetX + left, top, color);
    }

    @Override
    public void drawRectangle(int left, int top, int width, int height, int color, int border) {
        guiDraw.drawRectWithBorder(offsetX + left, top, offsetX + left + width, top + height, color, border);
    }
    
    @Override
    public void drawGradient(int left, int top, int width, int height, int color, int color2, int border) {
        guiDraw.drawGradientRectWithBorder(offsetX + left, top, offsetX + left + width, top + height, color, color2, border);
    }
    
    @Override
    public void drawStack(ItemStack stack, int left, int top, float scale) {
        guiDraw.drawStack(stack, offsetX + left, top, scale);
    }
}