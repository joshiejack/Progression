package joshie.crafting.gui;

import java.util.HashSet;
import java.util.Set;

import joshie.crafting.CraftAPIRegistry;
import joshie.crafting.CraftingMod;
import joshie.crafting.api.ICriteria;
import joshie.crafting.helpers.ClientHelper;
import net.minecraft.client.gui.GuiButton;

public class GuiCriteriaEditor extends GuiBase {
    public static final GuiCriteriaEditor INSTANCE = new GuiCriteriaEditor();
    private static Set<IRenderOverlay> overlays = new HashSet();
    public String originalName;
    public int y;

    public static void registerOverlay(IRenderOverlay overlay) {
        overlays.add(overlay);
    }

    @Override
    protected void actionPerformed(GuiButton button) {}

    @Override
    public void drawScreen(int i, int j, float f) {
        int x = 0;
        y = (height - ySize) / 2;
        drawRectWithBorder(x - 1, y, mc.displayWidth, y + ySize, 0xFFCCCCCC, 0xFF000000);
        selected.getCriteriaEditor().draw(x, y, offsetX);
        for (IRenderOverlay overlay : overlays) {
            if (overlay.isVisible()) {
                overlay.draw(x, y);
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
    public static boolean added = false;

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
            if(!selected.getCriteriaEditor().click(mouseX, mouseY, isDoubleClick)) {
                SelectTextEdit.INSTANCE.reset();
            }
        }

        //If we are trying to go back
        if (visible <= 1 && !clicked) {
            String originalName = GuiCriteriaEditor.INSTANCE.originalName;
            String newName = GuiCriteriaEditor.INSTANCE.selected.getUniqueName();
            if (!newName.equals(originalName) || newName.equals("NEW CRITERIA")) {
                ICriteria criteria = CraftAPIRegistry.criteria.get(newName);
                if (criteria == null) {
                    added = true;
                    CraftAPIRegistry.criteria.remove(originalName);
                    CraftAPIRegistry.criteria.put(newName, GuiCriteriaEditor.INSTANCE.selected);
                    //Add the criteria if it doesn't exist, and then continue
                } else if (!added) {
                    //If the new name is in the map, display this
                    InvalidName.INSTANCE.setDisplayed();
                    return; //Do not continue
                }
            }
            
            if (par3 == 1) {
                SelectTextEdit.INSTANCE.reset();
                ClientHelper.getPlayer().openGui(CraftingMod.instance, 0, null, 0, 0, 0);
            }
        }

        super.mouseClicked(par1, par2, par3);
    }
}
