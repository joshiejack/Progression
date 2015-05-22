package joshie.progression.criteria;

import java.util.ArrayList;
import java.util.List;

import joshie.progression.Progression;
import joshie.progression.api.ITriggerType;
import joshie.progression.api.ProgressionAPI;
import joshie.progression.gui.EditorCondition;
import joshie.progression.gui.GuiDrawHelper;
import joshie.progression.gui.GuiTriggerEditor;
import joshie.progression.helpers.ClientHelper;
import joshie.progression.json.Theme;
import joshie.progression.lib.ProgressionInfo;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class Trigger {
    @SideOnly(Side.CLIENT)
    private EditorCondition editor;

    private List<Condition> conditions = new ArrayList();
    private Criteria criteria;
    private ITriggerType triggerType;

    public Trigger(Criteria criteria, ITriggerType triggerType) {
        this.criteria = criteria;
        this.triggerType = triggerType;
        this.triggerType.markCriteria(criteria);
        //Don't load the editor server side
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
            this.editor = new EditorCondition(this);
        }
    }

    public ITriggerType getType() {
        return triggerType;
    }

    public void addCondition(Condition condition) {
        conditions.add(condition);
    }

    public EditorCondition getConditionEditor() {
        return editor;
    }

    public Criteria getCriteria() {
        return this.criteria;
    }

    public int getInternalID() {
        for (int id = 0; id < getCriteria().triggers.size(); id++) {
            Trigger aTrigger = getCriteria().triggers.get(id);
            if (aTrigger.equals(this)) return id;
        }

        return 0;
    }

    public List<Condition> getConditions() {
        return conditions;
    }

    protected int mouseX;
    protected int mouseY;
    protected int xPosition;

    public Result onClicked() {
        if (ClientHelper.canEdit()) {
            if (this.mouseX >= 87 && this.mouseX <= 97 && this.mouseY >= 4 && this.mouseY <= 14) {
                return Result.DENY; // Delete this trigger
            }
        }

        if (ClientHelper.canEdit() || this.getConditions().size() > 0) {
            if (this.mouseX >= 2 && this.mouseX <= 87) {
                if (this.mouseY >= 66 && this.mouseY <= 77) {

                    GuiTriggerEditor.INSTANCE.trigger = this;
                    ClientHelper.getPlayer().openGui(Progression.instance, 2, null, 0, 0, 0);
                    return Result.ALLOW;
                }
            }
        }

        return ClientHelper.canEdit() ? triggerType.onClicked(mouseX, mouseY) : Result.DEFAULT;
    }

    public void draw(int mouseX, int mouseY, int xPos) {
        this.mouseX = mouseX - xPosition;
        this.mouseY = mouseY - 45;
        this.xPosition = xPos + 6;
        GuiDrawHelper.INSTANCE.setOffset(xPosition, 45);
        ProgressionAPI.draw.drawGradient(1, 2, 99, 15, getType().getColor(), Theme.INSTANCE.triggerGradient1, Theme.INSTANCE.triggerGradient2);
        ProgressionAPI.draw.drawText(getType().getLocalisedName(), 6, 6, Theme.INSTANCE.triggerFontColor);

        if (ClientHelper.canEdit()) {
            int xXcoord = 234;
            if (this.mouseX >= 87 && this.mouseX <= 97 && this.mouseY >= 4 && this.mouseY <= 14) {
                xXcoord += 11;
            }

            ClientHelper.getMinecraft().getTextureManager().bindTexture(ProgressionInfo.textures);
            ProgressionAPI.draw.drawTexture(87, 4, xXcoord, 52, 11, 11);
        }

        triggerType.draw(this.mouseX, this.mouseY);

        int color = Theme.INSTANCE.blackBarBorder;
        if (this.mouseX >= 2 && this.mouseX <= 87) {
            if (this.mouseY >= 66 && this.mouseY <= 77) {
                color = Theme.INSTANCE.blackBarFontColor;
            }
        }

        if (ClientHelper.canEdit()) {
            ProgressionAPI.draw.drawGradient(2, 66, 85, 11, color, Theme.INSTANCE.blackBarGradient1, Theme.INSTANCE.blackBarGradient2);
            ProgressionAPI.draw.drawText("Condition Editor", 6, 67, Theme.INSTANCE.blackBarFontColor);
        } else if (this.getConditions().size() > 0) {
            ProgressionAPI.draw.drawGradient(2, 66, 85, 11, color, Theme.INSTANCE.blackBarGradient1, Theme.INSTANCE.blackBarGradient2);
            ProgressionAPI.draw.drawText("Condition Viewer", 6, 67, Theme.INSTANCE.blackBarFontColor);
        }
    }
}
