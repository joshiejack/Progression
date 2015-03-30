package joshie.crafting.gui;

import joshie.crafting.CraftAPIRegistry;
import joshie.crafting.CraftingRemapper;
import joshie.crafting.api.CraftingAPI;
import joshie.crafting.api.ICriteria;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class GuiTreeEditorEdit extends GuiTreeEditorDisplay {
    public static final GuiTreeEditorEdit INSTANCE = new GuiTreeEditorEdit();

    @Override
    protected void keyTyped(char character, int key) {
        ICriteria toRemove = null;
        for (ICriteria criteria : CraftAPIRegistry.criteria.values()) {
            if (criteria.getTreeEditor().keyTyped(character, key)) {
                toRemove = criteria;
                break;
            }
        }

        if (toRemove != null) {
            CraftAPIRegistry.removeCriteria(toRemove.getUniqueName());
        }

        super.keyTyped(character, key);
    }

    @Override
    public void mouseMovedOrUp(int x, int y, int button) {
        for (ICriteria criteria : CraftAPIRegistry.criteria.values()) {
            criteria.getTreeEditor().release(mouseX, mouseY);
        }
    }

    private long lastClick;
    private int lastType;
    private ICriteria lastClicked = null;

    @Override
    protected void mouseClicked(int par1, int par2, int par3) {
        long thisClick = System.currentTimeMillis();
        long difference = thisClick - lastClick;
        boolean isDoubleClick = par3 == 0 && lastType == 0 && lastClicked == previous && difference <= 500;
        lastClick = System.currentTimeMillis();
        lastType = par3;

        super.mouseClicked(par1, par2, par3);
        boolean clicked = false;
        for (ICriteria criteria : CraftAPIRegistry.criteria.values()) {
            if (criteria.getTreeEditor().click(mouseX, mouseY, isDoubleClick)) {
                if (!clicked) {
                    lastClicked = criteria;
                }
                
                clicked = true;
            }
        }

        if (!clicked && isDoubleClick) {
            previous = null;
            CraftingAPI.registry.newCriteria("NEW CRITERIA").getTreeEditor().setCoordinates(mouseX, mouseY);
        }
    }

    @Override
    public void handleMouseInput() {
        super.handleMouseInput();
        for (ICriteria criteria : CraftAPIRegistry.criteria.values()) {
            criteria.getTreeEditor().follow(mouseX, mouseY);
            int wheel = Mouse.getDWheel();
            if (wheel != 0) {
                criteria.getTreeEditor().scroll(wheel < 0);
            }
        }
    }
}
