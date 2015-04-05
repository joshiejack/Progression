package joshie.crafting.trigger;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import joshie.crafting.CraftingMod;
import joshie.crafting.api.Bus;
import joshie.crafting.api.ICondition;
import joshie.crafting.api.IConditionEditor;
import joshie.crafting.api.ICriteria;
import joshie.crafting.api.ITrigger;
import joshie.crafting.api.ITriggerData;
import joshie.crafting.gui.EditorCondition;
import joshie.crafting.gui.GuiCriteriaEditor;
import joshie.crafting.gui.GuiTriggerEditor;
import joshie.crafting.gui.SelectTextEdit;
import joshie.crafting.gui.SelectTextEdit.ITextEditable;
import joshie.crafting.helpers.ClientHelper;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.common.eventhandler.Event.Result;

public abstract class TriggerBase implements ITrigger {
    private static final ResourceLocation textures = new ResourceLocation("crafting", "textures/gui/textures.png");
    private List<ICondition> conditions = new ArrayList();
    private String typeName;
    private String localised;
    private int color;
    private ICriteria criteria;
    private IConditionEditor editor;

    public TriggerBase(String localised, int color, String typeName) {
        this.localised = localised;
        this.color = color;
        this.typeName = typeName;
        this.editor = new EditorCondition(this);
    }

    @Override
    public void addCondition(ICondition condition) {
        conditions.add(condition);
    }

    @Override
    public IConditionEditor getConditionEditor() {
        return editor;
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
    public Bus[] getEventBuses() {
        return new Bus[] { getBusType() };
    }

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
        GuiCriteriaEditor.INSTANCE.drawText(text, xPosition + x, y + 45, color);
    }

    protected void drawGradient(int x, int y, int width, int height, int color, int color2, int border) {
        GuiCriteriaEditor.INSTANCE.drawGradient(xPosition + x, y + 45, width, height, color, color2, border);
    }

    protected void drawBox(int x, int y, int width, int height, int color, int border) {
        GuiCriteriaEditor.INSTANCE.drawBox(xPosition + x, y + 45, width, height, color, border);
    }

    protected void drawStack(ItemStack stack, int x, int y, float scale) {
        GuiCriteriaEditor.INSTANCE.drawStack(stack, xPosition + x, y + 45, scale);
    }

    protected void drawTexture(int x, int y, int u, int v, int width, int height) {
        GuiCriteriaEditor.INSTANCE.drawTexture(xPosition + x, y + 45, u, v, width, height);
    }

    protected String getText(ITextEditable editable) {
        return SelectTextEdit.INSTANCE.getText(editable);
    }

    @Override
    public Result onClicked() {
        if (ClientHelper.canEdit()) {
            if (this.mouseX >= 88 && this.mouseX <= 95 && this.mouseY >= 4 && this.mouseY <= 14) {
                return Result.DENY; //Delete this trigger
            }
        }

        if (ClientHelper.canEdit() || this.getConditions().size() > 0) {
            if (this.mouseX >= 2 && this.mouseX <= 87) {
                if (this.mouseY >= 66 && this.mouseY <= 77) {

                    GuiTriggerEditor.INSTANCE.trigger = this;
                    ClientHelper.getPlayer().openGui(CraftingMod.instance, 2, null, 0, 0, 0);
                    return Result.ALLOW;
                }
            }
        }

        return ClientHelper.canEdit() ? clicked() : Result.DEFAULT;
    }

    public Result clicked() {
        return Result.DEFAULT;
    }

    protected void draw() {}

    @Override
    public void draw(int mouseX, int mouseY, int xPos) {
        this.mouseX = mouseX - xPosition;
        this.mouseY = mouseY - 45;
        this.xPosition = xPos + 6;

        drawGradient(1, 2, 99, 15, getColor(), 0xFF222222, 0xFF000000);
        drawText(getLocalisedName(), 6, 6, 0xFFFFFFFF);

        if (ClientHelper.canEdit()) {
            int xXcoord = 0;
            if (this.mouseX >= 87 && this.mouseX <= 97 && this.mouseY >= 4 && this.mouseY <= 14) {
                xXcoord = 11;
            }

            ClientHelper.getMinecraft().getTextureManager().bindTexture(textures);
            drawTexture(87, 4, xXcoord, 195, 11, 11);
        }

        draw();

        int color = 0xFF000000;
        if (this.mouseX >= 2 && this.mouseX <= 87) {
            if (this.mouseY >= 66 && this.mouseY <= 77) {
                color = 0xFFFFFFFF;
            }
        }

        if (ClientHelper.canEdit()) {
            drawGradient(2, 66, 85, 11, color, 0xFF222222, 0xFF000000);
            drawText("Condition Editor", 6, 67, 0xFFFFFFFF);
        } else if (this.getConditions().size() > 0) {
            drawGradient(2, 66, 85, 11, color, 0xFF222222, 0xFF000000);
            drawText("Condition Viewer", 6, 67, 0xFFFFFFFF);
        }

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
