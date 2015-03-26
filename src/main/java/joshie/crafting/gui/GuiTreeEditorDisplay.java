package joshie.crafting.gui;

import joshie.crafting.CraftAPIRegistry;
import joshie.crafting.api.ICriteria;

public class GuiTreeEditorDisplay extends GuiBase {
    public static final GuiTreeEditorDisplay INSTANCE = new GuiTreeEditorDisplay();
    protected int offsetX = 0;

    @Override
    public void drawScreen(int i, int j, float f) {
        int x = 0;
        int y = (height - ySize) / 2;
        super.drawScreen(i, j, f);
        for (ICriteria criteria : CraftAPIRegistry.criteria.values()) {
            criteria.getTreeEditor().draw(x, y, offsetX);
        }
    }
}

