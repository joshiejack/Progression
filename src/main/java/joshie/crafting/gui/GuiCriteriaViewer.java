package joshie.crafting.gui;

import joshie.crafting.helpers.ClientHelper;


public class GuiCriteriaViewer extends GuiBase {
    public static final GuiCriteriaViewer INSTANCE = new GuiCriteriaViewer();

    @Override
    public void drawBackground() {}
    
    @Override
    public void scroll(int amount) {}
    
    @Override
    public void drawForeground() {
        int x = (width - xSize) / 2;
        drawRectWithBorder(x, y, x + xSize, y + ySize, 0xEE121212, 0xFF000000);
        drawGradientRectWithBorder(x, y, x + xSize, y + 15, 0xFF222222, 0xFF000000, 0xFF000000);
        drawText(selected.getDisplayName(), x + 5, y + 4, 0xFFFFFFFF);
        mc.fontRenderer.drawSplitString("Some random ass long text, describing what we actually need to do, and why we need to do it. Perha,s with blah blah blah blah blah", x + 5, y + 20, xSize - 4, 0xFFFFFFFF);
        selected.getCriteriaViewer().draw(x, y);
    }

    @Override
    protected void keyTyped(char character, int key) {}
}
