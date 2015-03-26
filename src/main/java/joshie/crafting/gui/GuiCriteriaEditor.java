package joshie.crafting.gui;

import joshie.crafting.CraftingMod;
import joshie.crafting.helpers.ClientHelper;
import net.minecraft.client.gui.GuiButton;

import org.lwjgl.input.Mouse;

public class GuiCriteriaEditor extends GuiBase {
    public static final GuiCriteriaEditor INSTANCE = new GuiCriteriaEditor();

    @Override
    protected void actionPerformed(GuiButton button) {}

    @Override
    public void drawScreen(int i, int j, float f) {
        int x = (width - 430) / 2;
        int y = (height - ySize) / 2;
        drawRectWithBorder(x, y, x + xSize, y + ySize, 0xFF000000, 0xFFFFFFFF);
        selected.getCriteriaEditor().draw(x, y, 0);
    }

    @Override
    protected void keyTyped(char character, int key) {
        super.keyTyped(character, key);
        selected.getCriteriaEditor().keyTyped(character, key);
    }

    @Override
    public void mouseMovedOrUp(int x, int y, int button) {
        selected.getCriteriaEditor().release(mouseX, mouseY);
    }

    private static long lastClick;

    @Override
    protected void mouseClicked(int par1, int par2, int par3) {
        if (par3 == 1) {
            ClientHelper.getPlayer().openGui(CraftingMod.instance, 0, null, 0, 0, 0);
        }

        long thisClick = System.currentTimeMillis();
        long difference = thisClick - lastClick;
        boolean isDoubleClick = difference <= 350;
        lastClick = System.currentTimeMillis();

        selected.getCriteriaEditor().click(mouseX, mouseY, isDoubleClick);
        super.mouseClicked(par1, par2, par3);
    }

    @Override
    public void handleMouseInput() {
        super.handleMouseInput();
        selected.getCriteriaEditor().follow(mouseX, mouseY);
        int wheel = Mouse.getDWheel();
        if (wheel != 0) {
            selected.getCriteriaEditor().scroll(wheel < 0);
        }
    }
}
