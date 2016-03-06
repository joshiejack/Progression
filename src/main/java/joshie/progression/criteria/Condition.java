package joshie.progression.criteria;

import joshie.progression.api.IConditionType;
import joshie.progression.api.ProgressionAPI;
import joshie.progression.gui.base.DrawHelper;
import joshie.progression.helpers.MCClientHelper;
import joshie.progression.json.Theme;
import joshie.progression.lib.ProgressionInfo;
import net.minecraftforge.fml.common.eventhandler.Event.Result;

public class Condition {
    public boolean inverted = false;
    private Criteria criteria;
    private Trigger trigger;
    private IConditionType condition;

    public Condition(Criteria criteria, Trigger trigger, IConditionType condition, boolean inverted) {
        this.criteria = criteria;
        this.trigger = trigger;
        this.condition = condition;
        this.inverted = inverted;
    }

    public Criteria getCriteria() {
        return this.criteria;
    }

    public IConditionType getType() {
        return condition;
    }

    protected int xPosition;
    protected int mouseX;
    protected int mouseY;

    public Result onClicked() {
        if (MCClientHelper.canEdit()) {
            if (this.mouseX >= 88 && this.mouseX <= 95 && this.mouseY >= 4 && this.mouseY <= 14) {
                return Result.DENY; //Delete this trigger
            }

            if (mouseY >= 17 && mouseY <= 25 && this.mouseX >= 0 && this.mouseX <= 100) {
                inverted = !inverted;
                return Result.ALLOW;
            }
        }

        return MCClientHelper.canEdit() ? getType().onClicked(mouseX, mouseY) : Result.DEFAULT;
    }

    public void draw(int mouseX, int mouseY, int xPos) {
        this.mouseX = mouseX - xPosition;
        this.mouseY = mouseY - 45;
        this.xPosition = xPos + 6;

        DrawHelper.INSTANCE.setOffset(xPosition, 45);
        ProgressionAPI.draw.drawGradient(1, 2, 99, 15, getType().getColor(), Theme.INSTANCE.conditionGradient1, Theme.INSTANCE.conditionGradient2);
        ProgressionAPI.draw.drawText(getType().getLocalisedName(), 6, 6, Theme.INSTANCE.conditionFontColor);
        if (MCClientHelper.canEdit()) {
            int xXcoord = 234;
            if (this.mouseX >= 87 && this.mouseX <= 97 && this.mouseY >= 4 && this.mouseY <= 14) {
                xXcoord += 11;
            }

            MCClientHelper.getMinecraft().getTextureManager().bindTexture(ProgressionInfo.textures);
            ProgressionAPI.draw.drawTexture(87, 4, xXcoord, 52, 11, 11);
        }

        int color = Theme.INSTANCE.optionsFontColor;
        if (this.mouseY >= 17 && this.mouseY <= 25 && this.mouseX >= 0 && this.mouseX <= 100) {
            color = Theme.INSTANCE.optionsFontColorHover;
        }

        ProgressionAPI.draw.drawText("invert: " + inverted, 4, 18, color);
        getType().draw(mouseX, mouseY);
    }
}
