package joshie.crafting.gui;

import java.util.List;

import org.lwjgl.opengl.GL11;

import joshie.crafting.api.ICriteria;
import joshie.crafting.api.ICriteriaEditor;
import joshie.crafting.api.IReward;
import joshie.crafting.api.ITrigger;

public class EditorCriteria extends TextEditable implements ICriteriaEditor {
    private final ICriteria criteria;
    private ITextField selectedText;
    private int xCoord;
    private int yCoord;
    
    public EditorCriteria(ICriteria criteria) {
        this.criteria = criteria;
    }
    
    @Override
    public void drawText(String text, int x, int y, int color) {
        GuiTreeEditorEdit.INSTANCE.mc.fontRenderer.drawString(text, xCoord + x, yCoord + y, color);
    }
    
    @Override
    public void drawBox(int x, int y, int width, int height, int color, int border) {
        GuiTreeEditorEdit.INSTANCE.drawRectWithBorder(xCoord + x, yCoord + y, xCoord + x + width, yCoord + y + height, color, border);
    }
    
    private static int triggerStart = 0;
    private static int rewardStart = 0;

    @Override
    public void draw(int x, int y, int offsetX) {
        this.xCoord = x;
        this.yCoord = y;
        
        //Title and Repeatability Box
        String title = criteria.getUniqueName();
        String repeatibility = criteria.getRepeatAmount() + "x";
        drawBox(5, 5, 420, 15, 0xFF000000, 0xFFFFFFFF);
        drawText("Unique Name: " + title, 9, 9, 0xFFFFFFFF);
        drawText("Repeatability: " + repeatibility, 329, 9, 0xFF000000);
                
        //Triggers
        drawBox(5, 25, 420, 15, 0xFF000000, 0xFFFFFFFF);
        drawText("Triggers", 9, 29, 0xFFFFFFFF);
        drawBox(5, 39, 420, 75, 0xFF000000, 0xFFFFFFFF);
        int xCoord = 0;
        List<ITrigger> triggers = criteria.getTriggers();
        int size = triggerStart + 7;
        if (size > triggers.size()) {
            size = triggers.size();
        }
                
        for (int i = triggerStart; i < size; i++) {
            ITrigger trigger = triggers.get(i);
            int xPos = 100 * xCoord;
            trigger.draw(xPos);
            xCoord++;
        }
        
        drawText("ADD NEW TRIGGER", 9 + 100 * xCoord, 44, 0xFFFFFFFF);
        
        //Rewards
      //Triggers
        
        drawBox(5, 120, 420, 15, 0xFF000000, 0xFFFFFFFF);
        drawText("Rewards", 9, 124, 0xFFFFFFFF);
        drawBox(5, 134, 420, 75, 0xFF000000, 0xFFFFFFFF);
        xCoord = 0;
        List<IReward> rewards = criteria.getRewards();
        size = rewardStart + 7;
        if (size > rewards.size()) {
            size = rewards.size();
        }
                
        for (int i = rewardStart; i < size; i++) {
            int xPos = 100 * xCoord;
            IReward reward = rewards.get(i);
            reward.draw(xPos);
            xCoord++;
        }
        
        drawText("ADD NEW REWARD", 9 + 100 * xCoord, 139, 0xFFFFFFFF);
    }

    @Override
    public void click(int mouseX, int mouseY, boolean isDoubleClick) {
        
    }

    @Override
    public void release(int mouseX, int mouseY) {
        
    }

    @Override
    public void follow(int mouseX, int mouseY) {
        
    }

    @Override
    public void scroll(boolean b) {
        
    }

    @Override
    public void keyTyped(char character, int key) {
        super.keyTyped(character, key);
    }
    
    private static interface ITextField {
        public String getText();
        public void setText(String str);
    }

    @Override
    public String getTextField() {
        if (selectedText == null) return "";
        return selectedText.getText();
    }

    @Override
    public void setTextField(String str) {
        if (selectedText != null)
        selectedText.setText(str);
    }
}
