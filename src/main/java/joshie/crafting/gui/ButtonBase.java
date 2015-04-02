package joshie.crafting.gui;


import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

public abstract class ButtonBase extends GuiButton {
    protected static final ResourceLocation textures = new ResourceLocation("crafting", "textures/gui/textures.png");
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
    
    public static abstract class ButtonLeft extends ButtonBase {
        public ButtonLeft(int y) {
            super(0, 0, y, 25, 25, "");
        }
    }
    
    public static abstract class ButtonRight extends ButtonBase {
        public ButtonRight(int y) {
            super(0, GuiTreeEditor.INSTANCE.res.getScaledWidth() - 25, y, 25, 25, "");
        }
    }
}