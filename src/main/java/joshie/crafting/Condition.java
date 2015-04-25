package joshie.crafting;

import joshie.crafting.api.IConditionType;
import joshie.crafting.gui.GuiDrawHelper;
import joshie.crafting.gui.GuiTriggerEditor;
import joshie.crafting.gui.SelectTextEdit;
import joshie.crafting.gui.SelectTextEdit.ITextEditable;
import joshie.crafting.helpers.ClientHelper;
import joshie.crafting.json.Theme;
import joshie.crafting.lib.CraftingInfo;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.eventhandler.Event.Result;

public class Condition {
    protected boolean inverted = false;
    private Criteria criteria;
    private Trigger trigger;
    private IConditionType condition;

    public Condition(Criteria criteria, Trigger trigger, IConditionType condition) {
        this.criteria = criteria;
        this.trigger = trigger;
        this.condition = condition;
    }

    public Criteria getCriteria() {
        return this.criteria;
    }

    public IConditionType getType() {
        return condition;
    }

    public Condition setInversion(boolean inverted) {
        this.inverted = inverted;
        return this;
    }

    public boolean isInverted() {
        return inverted;
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

    public Result onClicked() {
        if (ClientHelper.canEdit()) {
            if (this.mouseX >= 88 && this.mouseX <= 95 && this.mouseY >= 4 && this.mouseY <= 14) {
                return Result.DENY; //Delete this trigger
            }

            if (mouseY >= 17 && mouseY <= 25 && this.mouseX >= 0 && this.mouseX <= 100) {
                inverted = !inverted;
                return Result.ALLOW;
            }
        }

        return ClientHelper.canEdit() ? getType().onClicked(mouseX, mouseY) : Result.DEFAULT;
    }

    public void draw(int mouseX, int mouseY, int xPos) {
        this.mouseX = mouseX - xPosition;
        this.mouseY = mouseY - 45;
        this.xPosition = xPos + 6;

        GuiDrawHelper.TriggerDrawHelper.INSTANCE.setOffset(xPosition, 45);
        drawGradient(1, 2, 99, 15, getType().getColor(), Theme.INSTANCE.conditionGradient1, Theme.INSTANCE.conditionGradient2);
        drawText(getType().getLocalisedName(), 6, 6, Theme.INSTANCE.conditionFontColor);
        int xXcoord = 0;
        if (this.mouseX >= 87 && this.mouseX <= 97 && this.mouseY >= 4 && this.mouseY <= 14) {
            xXcoord = 11;
        }

        int color = Theme.INSTANCE.optionsFontColor;
        if (this.mouseY >= 17 && this.mouseY <= 25 && this.mouseX >= 0 && this.mouseX <= 100) {
            color = Theme.INSTANCE.optionsFontColorHover;
        }

        drawText("invert: " + inverted, 4, 18, color);

        ClientHelper.getMinecraft().getTextureManager().bindTexture(CraftingInfo.textures);
        drawTexture(87, 4, xXcoord, 195, 11, 11);
        getType().draw(mouseX, mouseY);
    }
}
