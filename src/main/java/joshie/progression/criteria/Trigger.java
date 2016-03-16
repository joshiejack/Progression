package joshie.progression.criteria;

import java.util.ArrayList;
import java.util.List;

import joshie.progression.api.IFieldProvider;
import joshie.progression.api.ITriggerType;
import joshie.progression.gui.TriggerEditorElement;
import joshie.progression.gui.newversion.overlays.IDrawable;
import net.minecraftforge.fml.common.FMLCommonHandler;
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

    @Override
    public IFieldProvider getProvider() {
        return triggerType;
    }
}
