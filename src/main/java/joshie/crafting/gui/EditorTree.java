package joshie.crafting.gui;

import joshie.crafting.CraftingMod;
import joshie.crafting.api.ICriteria;
import joshie.crafting.api.ITreeEditor;
import joshie.crafting.api.IReward;
import joshie.crafting.helpers.ClientHelper;
import joshie.crafting.helpers.StackHelper;
import net.minecraft.item.ItemStack;

public class EditorTree extends TextEditable implements ITreeEditor {
    private final ICriteria criteria;
    private boolean isHeld;
    private int prevX;
    private int prevY;
    protected int left;
    protected int right;
    protected int top;
    protected int bottom;
    protected int width = 100;
    protected int height = 25;
    protected int offsetX = 0;
    private int x;
    private int y;
    
    public EditorTree(ICriteria criteria) {
        this.criteria = criteria;
    }
    
    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public void setCoordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    private void recalculate(int x) {
        left = this.x + x;
        right = (int) (this.x + width) + x;
        top = this.y;
        bottom = (int) (this.y + height);
        offsetX = x;
    }

    @Override
    public void draw(int x, int y, int offsetX) {
        recalculate(offsetX);
        //If We are in edit mode draw the boxes around the feature
        if (isSelected) {
            GuiTreeEditorEdit.INSTANCE.drawRectWithBorder(x + left, y + top, x + right, y + bottom, 0xFFFFFFFF, 0xFF00BFFF);
        } else GuiTreeEditorEdit.INSTANCE.drawRectWithBorder(x + left, y + top, x + right, y + bottom, 0xFFFFFFFF, 0xFF000000);
        
        GuiTreeEditorEdit.INSTANCE.mc.fontRenderer.drawString(getText(), x + left + 3, y + top + 3, 0xFF000000);
        
        //Draw in the rewards
        int xOffset = 0;
        for (IReward reward: criteria.getRewards()) {
            ItemStack icon = reward.getIcon();
            StackHelper.drawStack(icon, x + 2 + left + (xOffset * 12), y + top + 12, 0.75F);
            xOffset++;
        }
    }

    private boolean noOtherSelected() {
        return GuiTreeEditorEdit.INSTANCE.selected == null;
    }

    private void clearSelected() {
        GuiTreeEditorEdit.INSTANCE.selected = null;
    }

    @Override
    public void setSelected() {
        super.setSelected();
        
        GuiTreeEditorEdit.INSTANCE.selected = criteria;
    }

    private boolean isOver(int x, int y) {
        return x >= left && x <= right && y >= top && y <= bottom;
    }

    @Override
    public void click(int x, int y, boolean isDouble) {
        if (isOver(x, y) && noOtherSelected()) {
            if (isDouble) {
                GuiCriteriaEditor.INSTANCE.selected = criteria;
                ClientHelper.getPlayer().openGui(CraftingMod.instance, 1, null, 0, 0, 0);
                return;
            }
            
            isHeld = true;
            isSelected = true;
            prevX = x;
            prevY = y;
            setSelected();
        } else {
            if (isSelected && x >= 0) {
                clearSelected();
                isSelected = false;
            }
        }
    }

    @Override
    public void release(int x, int y) {
        if (isHeld) {
            isHeld = false;
            clearSelected();
        }
    }

    @Override
    public void follow(int x, int y) {
        if (isHeld) {
            this.x += x - prevX;
            this.y += y - prevY;
            prevX = x;
            prevY = y;
        }
    }

    @Override
    public void scroll(boolean scrolledDown) {
        return;
    }

    @Override
    public String getTextField() {
        return criteria.getUniqueName();
    }

    @Override
    public void setTextField(String str) {
        criteria.setUniqueName(str);
    }
}
