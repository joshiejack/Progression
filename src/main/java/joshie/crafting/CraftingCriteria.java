package joshie.crafting;

import java.util.ArrayList;
import java.util.List;

import joshie.crafting.api.ICriteria;
import joshie.crafting.api.IReward;
import joshie.crafting.api.ITrigger;
import joshie.crafting.gui.GuiCriteriaEditor;
import joshie.crafting.gui.GuiTreeEditorEdit;
import joshie.crafting.gui.TextEditable;
import joshie.crafting.helpers.ClientHelper;
import joshie.crafting.helpers.StackHelper;
import joshie.crafting.plugins.minetweaker.Criteria;
import minetweaker.MineTweakerAPI;
import net.minecraft.item.ItemStack;
import scala.actors.threadpool.Arrays;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.craftcontrol.Criteria")
public class CraftingCriteria extends TextEditable implements ICriteria {
    /** All the data for this **/
    private List<ITrigger> triggers = new ArrayList();
    private List<IReward> rewards = new ArrayList();
    private List<ICriteria> prereqs = new ArrayList();
    private List<ICriteria> conflicts = new ArrayList();
    private int isRepeatable;
    private int x;
    private int y;

    private String name;

    @ZenMethod
    public void add(String unique, String[] triggers, @Optional String[] rewards, @Optional String[] prereqs, @Optional String[] conflicts, @Optional boolean isRepeatable) {
        MineTweakerAPI.apply(new Criteria(unique, triggers, rewards, prereqs, conflicts, isRepeatable));
    }

    @Override
    public String getUniqueName() {
        return name;
    }

    @Override
    public ICriteria setUniqueName(String unique) {
        this.name = unique;
        return this;
    }

    @Override
    public ICriteria addTriggers(ITrigger... triggers) {
        this.triggers.addAll(Arrays.asList((ITrigger[]) triggers));
        return this;
    }

    @Override
    public ICriteria addRewards(IReward... rewards) {
        this.rewards.addAll(Arrays.asList((IReward[]) rewards));
        for (IReward reward : rewards) {
            reward.onAdded(this);
        }

        return this;
    }

    @Override
    public ICriteria addRequirements(ICriteria... prereqs) {
        this.prereqs.addAll(Arrays.asList((ICriteria[]) prereqs));
        return this;
    }

    @Override
    public ICriteria addConflicts(ICriteria... conflicts) {
        this.conflicts.addAll(Arrays.asList((ICriteria[]) conflicts));
        return this;
    }

    @Override
    public ICriteria setRepeatAmount(int amount) {
        this.isRepeatable = amount;
        return this;
    }

    @Override
    public List<ITrigger> getTriggers() {
        return triggers;
    }

    @Override
    public List<IReward> getRewards() {
        return rewards;
    }

    @Override
    public List<ICriteria> getRequirements() {
        return prereqs;
    }

    @Override
    public List<ICriteria> getConflicts() {
        return conflicts;
    }

    @Override
    public int getRepeatAmount() {
        return isRepeatable;
    }

    @Override
    public void setCoordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    /** IGNORE EVERYTHING BELOW THIS POINT, IT'S ALL FOR GUI EDITOR **/
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

    public void recalculate(int x) {
        left = this.x + x;
        right = (int) (this.x + width) + x;
        top = this.y;
        bottom = (int) (this.y + height);
        offsetX = x;
    }

    public void draw(int x, int y, int offsetX) {
        recalculate(offsetX);
        //If We are in edit mode draw the boxes around the feature
        if (isSelected) {
            GuiTreeEditorEdit.INSTANCE.drawRectWithBorder(x + left, y + top, x + right, y + bottom, 0xFFFFFFFF, 0xFF00BFFF);
        } else GuiTreeEditorEdit.INSTANCE.drawRectWithBorder(x + left, y + top, x + right, y + bottom, 0xFFFFFFFF, 0xFF000000);
        
        GuiTreeEditorEdit.INSTANCE.mc.fontRenderer.drawString(getText(), x + left + 3, y + top + 3, 0xFF000000);
        
        //Draw in the rewards
        int xOffset = 0;
        for (IReward reward: rewards) {
            ItemStack icon = reward.getIcon();
            StackHelper.drawStack(icon, x + 2 + left + (xOffset * 12), y + top + 12, 0.75F);
            xOffset++;
        }
    }

    private boolean noOtherSelected() {
        return GuiTreeEditorEdit.INSTANCE.selected == null;
    }

    public void clearSelected() {
        GuiTreeEditorEdit.INSTANCE.selected = null;
    }

    public void setSelected() {
        GuiTreeEditorEdit.INSTANCE.selected = this;
    }

    public boolean isOver(int x, int y) {
        return x >= left && x <= right && y >= top && y <= bottom;
    }

    public void click(int x, int y, boolean isDouble) {
        if (isOver(x, y) && noOtherSelected()) {
            if (isDouble) {
                GuiCriteriaEditor.INSTANCE.selected = this;
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

    public void release(int x, int y) {
        if (isHeld) {
            isHeld = false;
            clearSelected();
        }
    }

    public void follow(int x, int y) {
        if (isHeld) {
            this.x += x - prevX;
            this.y += y - prevY;
            prevX = x;
            prevY = y;
        }
    }

    public void scroll(boolean scrolledDown) {
        return;
    }

    @Override
    public String getTextField() {
        return getUniqueName();
    }

    @Override
    public void setTextField(String str) {
        setUniqueName(str);
    }
}
