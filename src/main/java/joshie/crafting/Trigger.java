package joshie.crafting;

import java.util.ArrayList;
import java.util.List;

import joshie.crafting.api.DrawHelper;
import joshie.crafting.api.ICondition;
import joshie.crafting.api.ITriggerType;
import joshie.crafting.gui.EditorCondition;
import joshie.crafting.gui.GuiDrawHelper;
import joshie.crafting.gui.GuiTriggerEditor;
import joshie.crafting.gui.SelectTextEdit;
import joshie.crafting.gui.SelectTextEdit.ITextEditable;
import joshie.crafting.helpers.ClientHelper;
import joshie.crafting.json.Theme;
import joshie.crafting.lib.CraftingInfo;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class Trigger {
    @SideOnly(Side.CLIENT)
    private EditorCondition editor;

    private List<ICondition> conditions = new ArrayList();
    private Criteria criteria;
    private ITriggerType triggerType;

    public Trigger(Criteria criteria, ITriggerType triggerType) {
        this.criteria = criteria;
        this.triggerType = triggerType;
        //Don't load the editor server side
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
            this.editor = new EditorCondition(this);
        }
    }

    public ITriggerType getType() {
        return triggerType;
    }

    public void addCondition(ICondition condition) {
        conditions.add(condition);
    }

    public EditorCondition getConditionEditor() {
        return editor;
    }

    public Criteria getCriteria() {
        return this.criteria;
    }

    public int getInternalID() {
        for (int id = 0; id < getCriteria().getTriggers().size(); id++) {
            Trigger aTrigger = getCriteria().getTriggers().get(id);
            if (aTrigger.equals(this)) return id;
        }

        return 0;
    }

    public Trigger setConditions(ICondition[] conditions) {
        for (ICondition condition : conditions) {
            this.conditions.add(condition);
        }

        return this;
    }

    public List<ICondition> getConditions() {
        return conditions;
    }

    protected int mouseX;
    protected int mouseY;
    protected int xPosition;

    protected String getText(ITextEditable editable) {
        return SelectTextEdit.INSTANCE.getText(editable);
    }

    public Result onClicked() {
        if (ClientHelper.canEdit()) {
            if (this.mouseX >= 88 && this.mouseX <= 95 && this.mouseY >= 4 && this.mouseY <= 14) {
                return Result.DENY; // Delete this trigger
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

        return ClientHelper.canEdit() ? triggerType.onClicked(mouseX, mouseY) : Result.DEFAULT;
    }

    public void draw(int mouseX, int mouseY, int xPos) {
        this.mouseX = mouseX - xPosition;
        this.mouseY = mouseY - 45;
        this.xPosition = xPos + 6;
        GuiDrawHelper.TriggerDrawHelper.INSTANCE.setOffset(xPosition, 45);
        DrawHelper.triggerDraw.drawGradient(1, 2, 99, 15, getType().getColor(), Theme.INSTANCE.triggerGradient1, Theme.INSTANCE.triggerGradient2);
        DrawHelper.triggerDraw.drawText(getType().getLocalisedName(), 6, 6, Theme.INSTANCE.triggerFontColor);

        if (ClientHelper.canEdit()) {
            int xXcoord = 0;
            if (this.mouseX >= 87 && this.mouseX <= 97 && this.mouseY >= 4 && this.mouseY <= 14) {
                xXcoord = 11;
            }

            ClientHelper.getMinecraft().getTextureManager().bindTexture(CraftingInfo.textures);
            DrawHelper.triggerDraw.drawTexture(87, 4, xXcoord, 195, 11, 11);
        }

        triggerType.draw(mouseX, mouseY);

        int color = Theme.INSTANCE.blackBarBorder;
        if (this.mouseX >= 2 && this.mouseX <= 87) {
            if (this.mouseY >= 66 && this.mouseY <= 77) {
                color = Theme.INSTANCE.blackBarFontColor;
            }
        }

        if (ClientHelper.canEdit()) {
            DrawHelper.triggerDraw.drawGradient(2, 66, 85, 11, color, Theme.INSTANCE.blackBarGradient1, Theme.INSTANCE.blackBarGradient2);
            DrawHelper.triggerDraw.drawText("Condition Editor", 6, 67, Theme.INSTANCE.blackBarFontColor);
        } else if (this.getConditions().size() > 0) {
            DrawHelper.triggerDraw.drawGradient(2, 66, 85, 11, color, Theme.INSTANCE.blackBarGradient1, Theme.INSTANCE.blackBarGradient2);
            DrawHelper.triggerDraw.drawText("Condition Viewer", 6, 67, Theme.INSTANCE.blackBarFontColor);
        }
    }
}
