package joshie.progression.gui.editors.insert;

import java.util.Collection;

import joshie.progression.api.criteria.ITrigger;
import joshie.progression.handlers.APIHandler;

public class FeatureNewTrigger extends FeatureNew<ITrigger> {
    public static final FeatureNewTrigger INSTANCE = new FeatureNewTrigger();

    public FeatureNewTrigger() {
        super("trigger");
    }

    @Override
    public Collection<ITrigger> getFields() {
        return APIHandler.triggerTypes.values();
    }

    @Override
    public void clone(ITrigger trigger) {
        APIHandler.cloneTrigger(criteria, trigger);
        //GuiCriteriaEditor.INSTANCE.initGui()
    }
}
