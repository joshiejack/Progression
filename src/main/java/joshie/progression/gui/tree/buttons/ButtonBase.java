package joshie.progression.gui.tree.buttons;


import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public abstract class ButtonBase extends GuiButton {
    public ButtonBase(int id, int xPosition, int yPosition, int width, int height, String name) {
        super(id, xPosition, yPosition, width, height, name);
    }

    @Override
    public boolean mousePressed(Minecraft mc, int x, int y) {
        if (super.mousePressed(mc, x, y)) {
            onClicked();
            return true;
        } else {
            onNotClicked();
            return false;
        }
    }

    public abstract void onClicked();
    
    public void onNotClicked() {
        
    }
}