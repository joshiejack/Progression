package joshie.progression.gui.editors.insert;

import joshie.progression.api.criteria.ITriggerProvider;
import joshie.progression.gui.editors.GuiCriteriaEditor;
import joshie.progression.handlers.APIHandler;

import java.util.Collection;

public class FeatureNewTrigger extends FeatureNew<ITriggerProvider> {
    public static final FeatureNewTrigger INSTANCE = new FeatureNewTrigger();

    public FeatureNewTrigger() {
        super("trigger");
    }

    @Override
    public Collection<ITriggerProvider> getFields() {
        return APIHandler.triggerTypes.values();
    }

    @Override
    public void clone(ITriggerProvider trigger) {
        APIHandler.cloneTrigger(GuiCriteriaEditor.INSTANCE.getCriteria(), trigger);
        //GuiCriteriaEditor.INSTANCE.initGui()
    }
}
