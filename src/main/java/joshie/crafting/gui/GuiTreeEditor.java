package joshie.crafting.gui;

import joshie.crafting.CraftAPIRegistry;
import joshie.crafting.api.ICriteria;

import org.lwjgl.input.Mouse;

public class GuiTreeEditor extends GuiBase {
    public static final GuiTreeEditor INSTANCE = new GuiTreeEditor();

    @Override
    protected void keyTyped(char character, int key) {
        super.keyTyped(character, key);
        for (ICriteria criteria : CraftAPIRegistry.criteria.values()) {
            criteria.keyTyped(character, key);
        }
    }

    @Override
    public void drawScreen(int i, int j, float f) {
        int x = (width - 430) / 2;
        int y = (height - ySize) / 2;
        super.drawScreen(i, j, f);
        for (ICriteria criteria : CraftAPIRegistry.criteria.values()) {
            criteria.draw(x, y);
        }
    }

    @Override
    public void mouseMovedOrUp(int x, int y, int button) {
        for (ICriteria criteria : CraftAPIRegistry.criteria.values()) {
            criteria.release(mouseX, mouseY);
        }
    }

    @Override
    protected void mouseClicked(int par1, int par2, int par3) {
        super.mouseClicked(par1, par2, par3);
        for (ICriteria criteria : CraftAPIRegistry.criteria.values()) {
            criteria.click(mouseX, mouseY);
        }
    }

    @Override
    public void handleMouseInput() {
        super.handleMouseInput();
        for (ICriteria criteria : CraftAPIRegistry.criteria.values()) {
            criteria.follow(mouseX, mouseY);
            int wheel = Mouse.getDWheel();
            if (wheel != 0) {
                criteria.scroll(wheel < 0);
            }
        }
    }
}
