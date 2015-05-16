package joshie.crafting.conditions;

import joshie.crafting.api.ICondition;
import joshie.crafting.api.ICriteria;
import joshie.crafting.api.ITrigger;
import joshie.crafting.gui.GuiTriggerEditor;
import joshie.crafting.gui.SelectTextEdit;
import joshie.crafting.gui.SelectTextEdit.ITextEditable;
import joshie.crafting.helpers.ClientHelper;
import joshie.crafting.json.Theme;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.common.eventhandler.Event.Result;

public abstract class ConditionBase implements ICondition {
    private static final ResourceLocation textures = new ResourceLocation("crafting", "textures/gui/textures.png");
    protected static final Theme theme = Theme.INSTANCE;
    private String typeName;
    protected boolean inverted = false;
    private ICriteria criteria;
    private ITrigger trigger;
    private String displayName;
    private int color;

    public ConditionBase(String localised, int color, String typeName) {
        this.typeName = typeName;
        this.color = color;
        this.displayName = localised;
    }

    @Override
    public String getTypeName() {
        return typeName;
    }

    @Override
    public ICondition setCriteria(ICriteria criteria) {
        this.criteria = criteria;
        return this;
    }

    @Override
    public ICriteria getCriteria() {
        return this.criteria;
    }

    @Override
    public ICondition setInversion(boolean inverted) {
        this.inverted = inverted;
        return this;
    }

    @Override
    public boolean isInverted() {
        return inverted;
    }

    @Override
    public ICondition setTrigger(ITrigger trigger) {
        this.trigger = trigger;
        return this;
    }

    @Override
    public String getLocalisedName() {
        return displayName;
    }

    protected int xPosition;
    protected int mouseX;
    protected int mouseY;

    protected void drawText(String text, int x, int y, int color) {
        GuiTriggerEditor.INSTANCE.trigger.getConditionEditor().drawText(text, xPosition + x, y + 45, color);
    }
    
    protected void drawSplitText(String text, int x, int y, int color, int width) {
        GuiTriggerEditor.INSTANCE.trigger.getConditionEditor().drawSplitText(text, xPosition + x, y + 45, color, width);
    }

    protected void drawGradient(int x, int y, int width, int height, int color, int color2, int border) {
        GuiTriggerEditor.INSTANCE.trigger.getConditionEditor().drawGradient(xPosition + x, y + 45, width, height, color, color2, border);
    }

    protected void drawBox(int x, int y, int width, int height, int color, int border) {
        GuiTriggerEditor.INSTANCE.trigger.getConditionEditor().drawBox(xPosition + x, y + 45, width, height, color, border);
    }

    protected void drawStack(ItemStack stack, int x, int y, float scale) {
        GuiTriggerEditor.INSTANCE.trigger.getConditionEditor().drawStack(stack, xPosition + x, y + 45, scale);
    }

    protected void drawTexture(int x, int y, int u, int v, int width, int height) {
        GuiTriggerEditor.INSTANCE.trigger.getConditionEditor().drawTexture(xPosition + x, y + 45, u, v, width, height);
    }

    protected String getText(ITextEditable editable) {
        return SelectTextEdit.INSTANCE.getText(editable);
    }

    @Override
    public Result onClicked() {
        if (this.mouseX >= 88 && this.mouseX <= 95 && this.mouseY >= 4 && this.mouseY <= 14) {
            return Result.DENY; //Delete this trigger
        }

        if (mouseY >= 17 && mouseY <= 25 && this.mouseX >= 0 && this.mouseX <= 100) {
            inverted = !inverted;
            return Result.ALLOW;
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
        this.mouseY = mouseY - 45;
        this.xPosition = xPos + 6;

        drawGradient(1, 2, 99, 15, getColor(), theme.conditionGradient1, theme.conditionGradient2);
        drawText(getLocalisedName(), 6, 6, theme.conditionFontColor);
        int xXcoord = 0;
        if (this.mouseX >= 87 && this.mouseX <= 97 && this.mouseY >= 4 && this.mouseY <= 14) {
            xXcoord = 11;
        }

        int color = theme.optionsFontColor;
        if (this.mouseY >= 17 && this.mouseY <= 25 && this.mouseX >= 0 && this.mouseX <= 100) {
            color = theme.optionsFontColorHover;
        }

        drawText("invert: " + inverted, 4, 18, color);

        ClientHelper.getMinecraft().getTextureManager().bindTexture(textures);
        drawTexture(87, 4, xXcoord, 195, 11, 11);
        draw();
    }

    private int getColor() {
        return color;
    }
}
