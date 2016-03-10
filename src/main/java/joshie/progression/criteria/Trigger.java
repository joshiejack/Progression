package joshie.progression.criteria;

import java.util.ArrayList;
import java.util.List;

import joshie.progression.Progression;
import joshie.progression.api.ITriggerType;
import joshie.progression.gui.GuiTriggerEditor;
import joshie.progression.gui.TriggerEditorElement;
import joshie.progression.gui.newversion.overlays.DrawFeatureHelper;
import joshie.progression.gui.newversion.overlays.IDrawable;
import joshie.progression.handlers.EventsManager;
import joshie.progression.helpers.ListHelper;
import joshie.progression.helpers.MCClientHelper;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Trigger implements IDrawable {
    @SideOnly(Side.CLIENT)
    private TriggerEditorElement editor;

    private List<Condition> conditions = new ArrayList();
    private Criteria criteria;
    private ITriggerType triggerType;
    private int ticker;

    public Trigger(Criteria criteria, ITriggerType triggerType) {
        this.criteria = criteria;
        this.triggerType = triggerType;
        this.triggerType.markCriteria(criteria);
        //Don't load the editor server side
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
            this.editor = new TriggerEditorElement(this);
        }
    }

    public ITriggerType getType() {
        return triggerType;
    }

    public void addCondition(Condition condition) {
        conditions.add(condition);
    }

    public TriggerEditorElement getConditionEditor() {
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
        if (MCClientHelper.canEdit()) {
            if (this.mouseX >= 87 && this.mouseX <= 97 && this.mouseY >= 4 && this.mouseY <= 14) {
                return Result.DENY; // Delete this trigger
            }
        }

        //Conditiion editor
        if (MCClientHelper.canEdit() || this.getConditions().size() > 0) {
            if (this.mouseX >= 2 && this.mouseX <= 87) {
                if (this.mouseY >= 66 && this.mouseY <= 77) {

                    GuiTriggerEditor.INSTANCE.trigger = this;
                    MCClientHelper.getPlayer().openGui(Progression.instance, 2, null, 0, 0, 0);
                    return Result.ALLOW;
                }
            }
        }

        return MCClientHelper.canEdit() ? triggerType.onClicked(mouseX, mouseY) : Result.DEFAULT;
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int xPos, int button) {
        return triggerType.onClicked(mouseX, mouseY) != Result.DEFAULT;
    }
    
    @Override
    public void remove(List list) {
    	EventsManager.onTriggerRemoved(this);
        ListHelper.remove(list, this);	
    }

    @Override
    public void draw(DrawFeatureHelper helper, int renderX, int renderY, int mouseX, int mouseY) {
        //For updating the render ticker
        ticker++;
        if (ticker == 0 || ticker >= 200) {
            triggerType.update();
            ticker = 1;
        }
        
        int width = MCClientHelper.isInEditMode() ? 99 : 79;
        helper.drawGradient(renderX, renderY, 1, 2, width, 15, getType().getColor(), helper.getTheme().triggerGradient1, helper.getTheme().triggerGradient2);
        helper.drawText(renderX, renderY, getType().getLocalisedName(), 6, 6, helper.getTheme().triggerFontColor);
        if (MCClientHelper.isInEditMode()) {
            triggerType.drawEditor(helper, renderX, renderY, mouseX, mouseY);
        } else {
            helper.drawSplitText(renderX, renderY, triggerType.getDescription(), 6, 20, 80, helper.getTheme().triggerFontColor);
            triggerType.drawDisplay(mouseX, mouseY);
        }
    	/*
    	ticker++;
    	if (ticker == 0 || ticker >= 200) {
    		triggerType.update();
    		ticker = 1;
    	}
    	
        this.mouseX = mouseX - xPosition;
        this.mouseY = mouseY - 45;
        this.xPosition = xPos + 6;
        DrawHelper.INSTANCE.setOffset(xPosition, 45);
        
        ProgressionAPI.draw.drawGradient(1, 2, width, 15, getType().getColor(), Theme.INSTANCE.triggerGradient1, Theme.INSTANCE.triggerGradient2);
        ProgressionAPI.draw.drawText(getType().getLocalisedName(), 6, 6, Theme.INSTANCE.triggerFontColor);
                
        

        if (!MCClientHelper.isInEditMode()) {
            
        } else  {
	        if (MCClientHelper.isInEditMode()) triggerType.drawEditor(this.mouseX, this.mouseY);
	        int color = Theme.INSTANCE.blackBarBorder;
	        if (this.mouseX >= 2 && this.mouseX <= 87) {
	            if (this.mouseY >= 66 && this.mouseY <= 77) {
	                color = Theme.INSTANCE.blackBarFontColor;
	            }
	        }
	
	        if (MCClientHelper.canEdit()) {
	            ProgressionAPI.draw.drawGradient(2, 66, 85, 11, color, Theme.INSTANCE.blackBarGradient1, Theme.INSTANCE.blackBarGradient2);
	            ProgressionAPI.draw.drawText("Condition Editor", 6, 67, Theme.INSTANCE.blackBarFontColor);
	        } else if (this.getConditions().size() > 0) {
	            ProgressionAPI.draw.drawGradient(2, 66, 85, 11, color, Theme.INSTANCE.blackBarGradient1, Theme.INSTANCE.blackBarGradient2);
	            ProgressionAPI.draw.drawText("Condition Viewer", 6, 67, Theme.INSTANCE.blackBarFontColor);
	        }
        } */	
    }
}
