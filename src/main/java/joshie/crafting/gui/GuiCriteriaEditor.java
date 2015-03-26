package joshie.crafting.gui;

import joshie.crafting.CraftingMod;
import joshie.crafting.helpers.ClientHelper;
import net.minecraft.client.gui.GuiButton;

public class GuiCriteriaEditor extends GuiBase {
    public static final GuiCriteriaEditor INSTANCE = new GuiCriteriaEditor();

    @Override
    protected void actionPerformed(GuiButton button) {}

    @Override
    protected void keyTyped(char character, int key) {
        super.keyTyped(character, key);
    }

    public void drawScreen(int i, int j, float f) {
        int x = (width - 430) / 2;
        int y = (height - ySize) / 2;
        super.drawScreen(i, j, f);
    }

    @Override
    protected void mouseClicked(int par1, int par2, int par3) {
        if (par3 == 1) {
            ClientHelper.getPlayer().openGui(CraftingMod.instance, 0, null, 0, 0, 0);
        }
        
        super.mouseClicked(par1, par2, par3);
    }
}
