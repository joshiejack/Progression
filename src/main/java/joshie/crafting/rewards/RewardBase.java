package joshie.crafting.rewards;

import joshie.crafting.api.Bus;
import joshie.crafting.api.ICriteria;
import joshie.crafting.api.IReward;
import joshie.crafting.gui.GuiCriteriaEditor;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.eventhandler.Event.Result;

public abstract class RewardBase implements IReward {
    private String localised;
    private String typeName;
    private int color;
    protected ICriteria criteria;

    public RewardBase(String localised, int color, String typeName) {
        this.localised = localised;
        this.color = color;
        this.typeName = typeName;
    }

    @Override
    public IReward setCriteria(ICriteria criteria) {
        this.criteria = criteria;
        return this;
    }

    @Override
    public ICriteria getCriteria() {
        return this.criteria;
    }

    @Override
    public String getTypeName() {
        return typeName;
    }
    
    @Override
    public String getLocalisedName() {
        return localised;
    }
    
    public int getColor() {
        return color;
    }

    @Override
    public Bus getBusType() {
        return Bus.NONE;
    }

    @Override
    public void onAdded() {}

    @Override
    public void onRemoved() {}

    protected int xPosition;
    protected int mouseX;
    protected int mouseY;

    protected void drawText(String text, int x, int y, int color) {
        GuiCriteriaEditor.INSTANCE.selected.getCriteriaEditor().drawText(text, xPosition + x, y + 135, color);
    }

    protected void drawBox(int x, int y, int width, int height, int color, int border) {
        GuiCriteriaEditor.INSTANCE.selected.getCriteriaEditor().drawBox(xPosition + x, y + 135, width, height, color, border);
    }

    protected void drawStack(ItemStack stack, int x, int y, float scale) {
        GuiCriteriaEditor.INSTANCE.selected.getCriteriaEditor().drawStack(stack, xPosition + x, y + 135, scale);
    }

    protected void draw() {
        
    }
    
    @Override
    public Result onClicked() {
        if (this.mouseX >= 88 && this.mouseX <= 95 && this.mouseY >= 4 && this.mouseY <= 14) {
            return Result.DENY; //Delete this reward
        }
        
        return clicked();
    }

    public Result clicked() {
        return Result.DEFAULT;
    }

    @Override
    public void draw(int mouseX, int mouseY, int xPos) {
        this.mouseX = mouseX - xPosition;
        this.mouseY = mouseY - 135;
        this.xPosition = xPos + 6;

        drawBox(1, 2, 99, 69, 0xFFFFFFFF, 0xFF000000);
        drawBox(1, 2, 99, 15, getColor(), 0xFF000000);
        drawText(getLocalisedName(), 6, 6, 0xFFFFFFFF);
        int color = 0xFFB20000;
        if (this.mouseX >= 88 && this.mouseX <= 95 && this.mouseY >= 4 && this.mouseY <= 14) {
            color = 0xFFFFFFFF;
        }
        
        drawText("X", 90, 6, color);
        draw();
    }
}
