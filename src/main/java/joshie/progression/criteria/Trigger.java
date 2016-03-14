package joshie.progression.criteria;

import java.util.ArrayList;
import java.util.List;

import joshie.progression.api.ICancelable;
import joshie.progression.api.IField;
import joshie.progression.api.ITriggerType;
import joshie.progression.gui.TriggerEditorElement;
import joshie.progression.gui.newversion.overlays.IDrawable;
import joshie.progression.handlers.EventsManager;
import joshie.progression.helpers.CollectionHelper;
import joshie.progression.json.Theme;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Trigger implements IDrawable, ICancelable {
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

    @Override
    public void remove(List list) {
        EventsManager.onTriggerRemoved(this);
        CollectionHelper.remove(list, this); //Remove from temporary list    
        CollectionHelper.remove(criteria.triggers, this); //Remove from real list
    }
    
    @Override
    public void update() {
        triggerType.update();
    }
    
    @Override
    public int getColor() {
        return triggerType.getColor();
    }
    
    @Override
    public int getGradient1() {
        return Theme.INSTANCE.triggerGradient1;
    }
    
    @Override
    public int getGradient2() {
        return Theme.INSTANCE.triggerGradient2;
    }
    
    @Override
    public int getFontColor() {
        return Theme.INSTANCE.triggerFontColor;
    }
    
    @Override
    public String getLocalisedName() {
        return triggerType.getLocalisedName();
    }

    @Override
    public String getDescription() {
        return triggerType.getDescription();
    }

    @Override
    public void drawDisplay(int mouseX, int mouseY) {
        triggerType.drawDisplay(mouseX, mouseY);
    }

    @Override
    public List<IField> getFields() {
        return triggerType.getFields();
    }

    @Override
    public boolean isCanceling() {
        return triggerType.isCanceling();
    }

    @Override
    public boolean isCancelable() {
        return triggerType.isCancelable();
    }

    @Override
    public void setCanceling(boolean isCanceld) {
        triggerType.setCanceling(isCanceld);
    }
}
