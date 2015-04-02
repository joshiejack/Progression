package joshie.crafting.gui;

import java.util.HashSet;
import java.util.Set;

import joshie.crafting.CraftingMod;
import joshie.crafting.helpers.ClientHelper;

public class GuiCriteriaEditor extends GuiBase {
    public static final GuiCriteriaEditor INSTANCE = new GuiCriteriaEditor();
    protected static Set<IRenderOverlay> overlays = new HashSet();

    public static void registerOverlay(IRenderOverlay overlay) {
        overlays.add(overlay);
    }

    @Override
    public void drawForeground() {
        selected.getCriteriaEditor().draw(0, y, offsetX);
        for (IRenderOverlay overlay : overlays) {
            if (overlay.isVisible()) {
                overlay.draw(0, y);
            }
        }
    }

    @Override
    protected void keyTyped(char character, int key) {
        super.keyTyped(character, key);

        for (IRenderOverlay overlay : overlays) {
            if (overlay.isVisible()) {
                overlay.keyTyped(character, key);
            }
        }
    }

    private static long lastClick;

    @Override
    protected void mouseClicked(int par1, int par2, int par3) {
        long thisClick = System.currentTimeMillis();
        long difference = thisClick - lastClick;
        boolean isDoubleClick = difference <= 150;
        lastClick = System.currentTimeMillis();

        boolean clicked = false;
        int visible = 0;
        for (IRenderOverlay overlay : overlays) {
            if (overlay.isVisible()) {
                if (overlay.mouseClicked(mouseX, mouseY, par3)) {
                    clicked = true;
                    break;
                }

                visible++;
            }
        }

        if (!clicked) {
            if (!selected.getCriteriaEditor().click(mouseX, mouseY, isDoubleClick)) {
                SelectTextEdit.INSTANCE.reset();
            }
        }

        //If we are trying to go back
        if (visible <= 1 && !clicked) {
            if (par3 == 1) {
                GuiTreeEditor.INSTANCE.currentTab = GuiCriteriaEditor.INSTANCE.selected.getTabID();
                GuiTreeEditor.INSTANCE.currentTabName = GuiTreeEditor.INSTANCE.currentTab.getUniqueName();
                SelectTextEdit.INSTANCE.reset();
                GuiTreeEditor.INSTANCE.selected = null;
                GuiTreeEditor.INSTANCE.previous = null;
                GuiTreeEditor.INSTANCE.lastClicked = null;
                ClientHelper.getPlayer().openGui(CraftingMod.instance, 0, null, 0, 0, 0);
            }
        }

        super.mouseClicked(par1, par2, par3);
    }
}
