package joshie.progression.gui.editors.insert;

import java.util.Collection;

import joshie.progression.api.criteria.IProgressionTrigger;
import joshie.progression.handlers.APIHandler;

public class FeatureNewTrigger extends FeatureNew<IProgressionTrigger> {
    public static final FeatureNewTrigger INSTANCE = new FeatureNewTrigger();

    public FeatureNewTrigger() {
        super("trigger");
    }

    @Override
    public Collection<IProgressionTrigger> getFields() {
        return APIHandler.triggerTypes.values();
    }

    @Override
    public void clone(IProgressionTrigger trigger) {
        APIHandler.cloneTrigger(criteria, trigger);
        //GuiCriteriaEditor.INSTANCE.initGui()
    }
}
