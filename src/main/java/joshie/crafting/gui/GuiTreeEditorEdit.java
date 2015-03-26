package joshie.crafting.gui;

import joshie.crafting.CraftAPIRegistry;
import joshie.crafting.api.ICriteria;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class GuiTreeEditorEdit extends GuiTreeEditorDisplay {
    public static final GuiTreeEditorEdit INSTANCE = new GuiTreeEditorEdit();

    @Override
    protected void keyTyped(char character, int key) {
        int jump = 1;
        if (Keyboard.isKeyDown(54) || Keyboard.isKeyDown(42)) {
            jump = 50;
        }
        
        if (key == 203) {
            offsetX += jump;
            if (offsetX >= 0) {
                offsetX = 0;
            }
        } else if (key == 205) {
            offsetX -= jump;
        }
        
        super.keyTyped(character, key);
        for (ICriteria criteria : CraftAPIRegistry.criteria.values()) {
            criteria.keyTyped(character, key);
        }
    }

    @Override
    public void mouseMovedOrUp(int x, int y, int button) {
        for (ICriteria criteria : CraftAPIRegistry.criteria.values()) {
            criteria.release(mouseX, mouseY);
        }
    }
    
    private long lastClick;

    @Override
    protected void mouseClicked(int par1, int par2, int par3) {
        long thisClick = System.currentTimeMillis();
        long difference = thisClick - lastClick;
        boolean isDoubleClick = difference <= 350;
        lastClick = System.currentTimeMillis();
        
        super.mouseClicked(par1, par2, par3);
        for (ICriteria criteria : CraftAPIRegistry.criteria.values()) {
            criteria.click(mouseX, mouseY, isDoubleClick);
        }
    }

    @Override
    public void handleMouseInput() {
        super.handleMouseInput();
        for (ICriteria criteria : CraftAPIRegistry.criteria.values()) {
            criteria.follow(mouseX, mouseY);
            int wheel = Mouse.getDWheel();
            if (wheel != 0) {
                criteria.scroll(wheel < 0);
            }
        }
    }
}
