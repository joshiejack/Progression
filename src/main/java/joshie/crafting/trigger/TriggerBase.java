package joshie.crafting.trigger;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import joshie.crafting.api.Bus;
import joshie.crafting.api.ICondition;
import joshie.crafting.api.ICriteria;
import joshie.crafting.api.ITrigger;
import joshie.crafting.api.ITriggerData;
import joshie.crafting.gui.GuiCriteriaEditor;
import joshie.crafting.gui.SelectTextEdit;
import joshie.crafting.gui.SelectTextEdit.ITextEditable;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.eventhandler.Event.Result;

public abstract class TriggerBase implements ITrigger {
    private List<ICondition> conditions = new ArrayList();
    private String typeName;
    private String localised;
    private int color;
    private ICriteria criteria;

    public TriggerBase(String localised, int color, String typeName) {
        this.localised = localised;
        this.color = color;
        this.typeName = typeName;
    }

    @Override
    public ITrigger setCriteria(ICriteria criteria) {
        this.criteria = criteria;
        return this;
    }

    @Override
    public ICriteria getCriteria() {
        return this.criteria;
    }

    @Override
    public Bus getBusType() {
        return Bus.FORGE;
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
    public int getInternalID() {
        for (int id = 0; id < getCriteria().getTriggers().size(); id++) {
            ITrigger aTrigger = getCriteria().getTriggers().get(id);
            if (aTrigger.equals(this)) return id;
        }

        return 0;
    }

    @Override
    public ITrigger setConditions(ICondition[] conditions) {
        for (ICondition condition : conditions) {
            this.conditions.add(condition);
        }

        return this;
    }

    @Override
    public List<ICondition> getConditions() {
        return conditions;
    }

    @Override
    public void onFired(UUID uuid, ITriggerData triggerData, Object... data) {
        onFired(triggerData, data);
    }

    public void onFired(ITriggerData triggerData, Object... data) {}

    protected int xPosition;
    protected int mouseX;
    protected int mouseY;

    protected void drawText(String text, int x, int y, int color) {
        GuiCriteriaEditor.INSTANCE.selected.getCriteriaEditor().drawText(text, xPosition + x, y + 40, color);
    }

    protected void drawBox(int x, int y, int width, int height, int color, int border) {
        GuiCriteriaEditor.INSTANCE.selected.getCriteriaEditor().drawBox(xPosition + x, y + 40, width, height, color, border);
    }

    protected void drawStack(ItemStack stack, int x, int y, float scale) {
        GuiCriteriaEditor.INSTANCE.selected.getCriteriaEditor().drawStack(stack, xPosition + x, y + 40, scale);
    }
    
    protected String getText(ITextEditable editable) {
        return SelectTextEdit.INSTANCE.getText(editable);
    }

    @Override
    public Result onClicked() {
        if (this.mouseX >= 88 && this.mouseX <= 95 && this.mouseY >= 4 && this.mouseY <= 14) {
            return Result.DENY; //Delete this trigger
        }

        return clicked();
    }

    public Result clicked() {
        return Result.DEFAULT;
    }

    protected void draw() {}

    @Override
    public void draw(int mouseX, int mouseY, int xPos) {
        this.mouseX = mouseX - xPosition;
        this.mouseY = mouseY - 40;
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

    /** A whole bunch of convenience methods **/

    //Shorthand
    protected Block asBlock(Object[] object) {
        return asBlock(object, 0);
    }

    //Normalhand
    protected Block asBlock(Object[] object, int index) {
        return (Block) object[index];
    }

    //Shorthand
    protected String asString(Object[] object) {
        return asString(object, 0);
    }

    //Normalhand
    protected String asString(Object[] object, int index) {
        return asString(object, 0, "");
    }

    //Longhand
    protected String asString(Object[] object, int index, String theDefault) {
        if (object != null) {
            return (String) object[index];
        } else return theDefault;
    }

    //Shorthand
    protected int asInt(Object[] object) {
        return asInt(object, 0);
    }

    //Normalhand
    protected int asInt(Object[] object, int index) {
        return asInt(object, index, 0);
    }

    //Longhand
    protected int asInt(Object[] object, int index, int theDefault) {
        if (object != null) {
            return (Integer) object[index];
        } else return theDefault;
    }

    //Shorthand
    protected boolean asBoolean(Object[] object) {
        return asBoolean(object, 0);
    }

    //Normalhand
    protected boolean asBoolean(Object[] object, int index) {
        return asBoolean(object, index, true);
    }

    //Longhand
    protected boolean asBoolean(Object[] object, int index, boolean theDefault) {
        if (object != null) {
            return (Boolean) object[index];
        } else return theDefault;
    }

    //Shorthand
    protected ItemStack asStack(Object[] object) {
        return asStack(object, 0);
    }

    //Normalhand
    protected ItemStack asStack(Object[] object, int index) {
        return (ItemStack) object[index];
    }
}
