package joshie.crafting.gui;

import net.minecraft.item.ItemStack;

public class OverlayBase implements IRenderOverlay {
    public OverlayBase() {
        GuiCriteriaEditor.registerOverlay(this);
    }
    
    public void drawBox(int x, int y, int width, int height, int color, int border) {
        GuiCriteriaEditor.INSTANCE.selected.getCriteriaEditor().drawBox(x, y, width, height, color, border);
    }
    
    public void drawText(String text, int x, int y, int color) {
        GuiCriteriaEditor.INSTANCE.selected.getCriteriaEditor().drawText(text, x, y, color);
    }
    
    public void drawStack(ItemStack stack, int x, int y, float size) {
        GuiCriteriaEditor.INSTANCE.selected.getCriteriaEditor().drawStack(stack, x, y, size);
    }
    
    //Resets
    public boolean reset() {
        if (InvalidName.INSTANCE.isDisplayed()) {
            return false;
        }
        
        InvalidName.INSTANCE.clear();
        SelectItemOverlay.INSTANCE.clear();
        SelectTextEdit.INSTANCE.clear();
        SelectEntity.INSTANCE.clear();
        NewTrigger.INSTANCE.clear();
        NewReward.INSTANCE.clear();
        return true;
    }
    
    void clear() {}
    
    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public boolean keyTyped(char character, int key) {
        return false;
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int button) {
        return false;
    }

    @Override
    public void draw(int x, int y) { }
}
