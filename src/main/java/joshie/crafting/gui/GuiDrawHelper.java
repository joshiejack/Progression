package joshie.crafting.gui;

import joshie.crafting.api.DrawHelper.IDrawHelper;
import net.minecraft.item.ItemStack;

public class GuiDrawHelper {
    public static class TriggerDrawHelper implements IDrawHelper {
        public static final TriggerDrawHelper INSTANCE = new TriggerDrawHelper();
        
        private int xPosition = 0;
        public void setXPosition(int x) {
            this.xPosition = x;
        }
        
        public void drawText(String text, int x, int y, int color) {
            GuiCriteriaEditor.INSTANCE.drawText(text, xPosition + x, y + 45, color);
        }

        @Override
        public void drawGradient(int x, int y, int width, int height, int color, int color2, int border) {
            GuiCriteriaEditor.INSTANCE.drawGradient(xPosition + x, y + 45, width, height, color, color2, border);
        }

        public void drawBox(int x, int y, int width, int height, int color, int border) {
            GuiCriteriaEditor.INSTANCE.drawBox(xPosition + x, y + 45, width, height, color, border);
        }

        public void drawStack(ItemStack stack, int x, int y, float scale) {
            GuiCriteriaEditor.INSTANCE.drawStack(stack, xPosition + x, y + 45, scale);
        }

        public void drawTexture(int x, int y, int u, int v, int width, int height) {
            GuiCriteriaEditor.INSTANCE.drawTexture(xPosition + x, y + 45, u, v, width, height);
        }

        public void tick(int mouseX, int mouseY, int xPosition) {
            setXPosition(xPosition);
        }

        @Override
        public int getXPosition() {
            return xPosition;
        }
    }
}
