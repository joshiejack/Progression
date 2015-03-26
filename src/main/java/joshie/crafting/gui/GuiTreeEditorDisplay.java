package joshie.crafting.gui;

import joshie.crafting.CraftAPIRegistry;
import joshie.crafting.api.ICriteria;

public class GuiTreeEditorDisplay extends GuiBase {
    public static final GuiTreeEditorDisplay INSTANCE = new GuiTreeEditorDisplay();

    @Override
    public void drawScreen(int i, int j, float f) {
        int x = (width - 430) / 2;
        int y = (height - ySize) / 2;
        super.drawScreen(i, j, f);
        for (ICriteria criteria : CraftAPIRegistry.criteria.values()) {
            criteria.draw(x, y);
        }
    }
}

