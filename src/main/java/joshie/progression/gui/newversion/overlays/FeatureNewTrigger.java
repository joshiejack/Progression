package joshie.progression.gui.newversion.overlays;

import java.util.Collection;

import joshie.progression.api.ITriggerType;
import joshie.progression.handlers.APIHandler;

public class FeatureNewTrigger extends FeatureNew<ITriggerType> {
    public static final FeatureNewTrigger INSTANCE = new FeatureNewTrigger();

    public FeatureNewTrigger() {
        super("trigger");
    }

    @Override
    public Collection<ITriggerType> getFields() {
        return APIHandler.triggerTypes.values();
    }

    @Override
    public void clone(ITriggerType t) {
        APIHandler.cloneTrigger(criteria, trigger);
        //GuiCriteriaEditor.INSTANCE.initGui()
    }
}
