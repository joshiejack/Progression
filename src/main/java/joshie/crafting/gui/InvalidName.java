package joshie.crafting.gui;

import org.lwjgl.opengl.GL11;

public class InvalidName extends OverlayBase {
    public static InvalidName INSTANCE = new InvalidName();
    private static boolean visible = false;
    
    public void setDisplayed () {
        visible = true;
    }

    @Override
    public void clear() {}

    @Override
    public boolean isVisible() {
        return true;
    }

    public boolean isDisplayed() {
        return visible;
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int button) {
        if (visible) {
            visible = false;
            return true;
        }

        return false;
    }

    @Override
    public void draw(int x, int y) {
        if (visible) {
            GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
            int mouseX = GuiCriteriaEditor.INSTANCE.mouseX;
            int mouseY = GuiCriteriaEditor.INSTANCE.mouseY;
            drawBox(-GuiCriteriaEditor.INSTANCE.offsetX + 150, 30, 200, 15, 0xFF00008C, 0xFF000000);
            drawText("THIS NAME IS TAKEN, CHOOSE ANOTHER", -GuiCriteriaEditor.INSTANCE.offsetX + 155, 34, 0xFFFFFFFF);
        }
    }
}
