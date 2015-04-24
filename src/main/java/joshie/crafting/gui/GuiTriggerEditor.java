package joshie.crafting.gui;

import java.util.HashSet;
import java.util.Set;

import joshie.crafting.CraftingMod;
import joshie.crafting.Trigger;
import joshie.crafting.helpers.ClientHelper;

public class GuiTriggerEditor extends GuiBase {
    public static final GuiTriggerEditor INSTANCE = new GuiTriggerEditor();
    protected static Set<IRenderOverlay> overlays = new HashSet();
    public static Trigger trigger;

    public static void registerOverlay(IRenderOverlay overlay) {
        overlays.add(overlay);
    }

    @Override
    public void drawForeground() {
        trigger.getConditionEditor().draw(0, y, offsetX);
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
            if (!trigger.getConditionEditor().click(mouseX, mouseY, isDoubleClick)) {
                SelectTextEdit.INSTANCE.reset();
            }
        }

        //If we are trying to go back
        if (visible <= 1 && !clicked) {
            if (par3 == 1) {
                SelectTextEdit.INSTANCE.reset();
                ClientHelper.getPlayer().openGui(CraftingMod.instance, 1, null, 0, 0, 0);
            }
        }

        super.mouseClicked(par1, par2, par3);
    }
}
