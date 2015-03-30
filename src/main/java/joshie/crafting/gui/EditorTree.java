package joshie.crafting.gui;

import java.util.Iterator;
import java.util.List;

import joshie.crafting.CraftingMod;
import joshie.crafting.api.CraftingAPI;
import joshie.crafting.api.ICriteria;
import joshie.crafting.api.IReward;
import joshie.crafting.api.ITreeEditor;
import joshie.crafting.helpers.ClientHelper;
import joshie.crafting.helpers.StackHelper;
import net.minecraft.item.ItemStack;

import org.lwjgl.input.Keyboard;

public class EditorTree implements ITreeEditor {
    private final ICriteria criteria;
    private boolean isSelected;
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

    public void draw(int x, int y, int offsetX) {
        draw(x, y, offsetX, 0);
    }

    @Override
    public void draw(int x, int y, int offsetX, int highlight) {
        recalculate(offsetX);
        if (highlight != 0) {
            GuiTreeEditorEdit.INSTANCE.drawRectWithBorder(x + left, y + top, x + right, y + bottom, 0x00FFFFFF, highlight);
        } else {
            int uncompleted = 0xFFFFFFFF;
            int completed = 0xFF99FF99;
            int outofbounds = 0xFFB973FF;
            boolean isCompleted = CraftingAPI.players.getClientPlayer().getMappings().getCompletedCriteria().containsKey(criteria);
            int color = isCompleted? completed: uncompleted;
            if (criteria.getTreeEditor().getY() < 0) {
                color = outofbounds;
            }
                        
            //If We are in edit mode draw the boxes around the feature
            if (isSelected) {
                GuiTreeEditorEdit.INSTANCE.drawRectWithBorder(x + left, y + top, x + right, y + bottom, color, 0xFF00BFFF);
            } else GuiTreeEditorEdit.INSTANCE.drawRectWithBorder(x + left, y + top, x + right, y + bottom, color, 0xFF000000);

            GuiTreeEditorEdit.INSTANCE.mc.fontRenderer.drawString(criteria.getUniqueName(), x + left + 3, y + top + 3, 0xFF000000);

            //Draw in the rewards
            int xOffset = 0;
            for (IReward reward : criteria.getRewards()) {
                ItemStack icon = reward.getIcon();
                if (icon == null || icon.getItem() == null) continue; //Protection against null icons
                StackHelper.drawStack(icon, x + 2 + left + (xOffset * 12), y + top + 12, 0.75F);
                xOffset++;
            }
        }
    }

    private boolean noOtherSelected() {
        return GuiTreeEditorEdit.INSTANCE.selected == null;
    }

    private void clearSelected() {
        GuiTreeEditorEdit.INSTANCE.selected = null;
    }

    private void setSelected() {
        GuiTreeEditorEdit.INSTANCE.selected = criteria;
        GuiTreeEditorEdit.INSTANCE.previous = criteria;
    }

    private ICriteria getPrevious() {
        return GuiTreeEditorEdit.INSTANCE.previous;
    }

    private boolean isOver(int x, int y) {
        return x >= left && x <= right && y >= top && y <= bottom;
    }

    private void remove(List<ICriteria> list, ICriteria criteria) {
        Iterator<ICriteria> it = list.iterator();
        while (it.hasNext()) {
            ICriteria c = it.next();
            if (c.equals(criteria)) {
                it.remove();
                break;
            }
        }
    }
    

    @Override
    public boolean keyTyped(char character, int key) {
        if (isSelected) {
            return key == 211 || key == 14;
        }
        
        return false;
    }

    @Override
    public boolean click(int x, int y, boolean isDouble) {
        if (isOver(x, y)) {
            if (noOtherSelected()) {
                ICriteria previous = getPrevious();
                if (previous != null) {
                    List<ICriteria> list = null;
                    boolean isConflict = false;
                    if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                        list = previous.getRequirements();
                        if (previous.getConflicts().contains(criteria)) {
                            list = null;
                        }
                    } else if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
                        list = previous.getConflicts();
                        isConflict = true;
                    }

                    if (list != null) {
                        if (list.contains(criteria)) {
                            remove(list, criteria);
                            if (isConflict) {
                                remove(criteria.getConflicts(), previous);
                            }
                        } else {
                            //Now that it's been added, move everything
                            list.add(criteria);
                            if (isConflict) {
                                criteria.getConflicts().add(previous);
                            }
                        }

                        return true;
                    }
                }

                if (isDouble) {
                    GuiCriteriaEditor.INSTANCE.selected = criteria;
                    GuiCriteriaEditor.INSTANCE.originalName = criteria.getUniqueName();
                    GuiCriteriaEditor.INSTANCE.added = false;
                    ClientHelper.getPlayer().openGui(CraftingMod.instance, 1, null, 0, 0, 0);
                    return true;
                }

                isHeld = true;
                isSelected = true;
                prevX = x;
                prevY = y;
                setSelected();
                return true;
            }

            return false;
        } else {
            if (isSelected && x >= 0) {
                clearSelected();
                isSelected = false;
            }

            return false;
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
}
