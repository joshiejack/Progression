package joshie.progression.gui.editors.insert;

import joshie.progression.api.criteria.ITriggerProvider;
import joshie.progression.handlers.APIHandler;
import joshie.progression.handlers.RuleHandler;

import java.util.Collection;

import static joshie.progression.gui.core.GuiList.CRITERIA_EDITOR;

public class FeatureNewTrigger extends FeatureNew<ITriggerProvider> {

    public FeatureNewTrigger() {
        super("trigger");
    }

    @Override
    public Collection<ITriggerProvider> getFields() {
        return APIHandler.triggerTypes.values();
    }

    @Override
    public void clone(ITriggerProvider trigger) {
        RuleHandler.cloneTrigger(CRITERIA_EDITOR.get(), trigger);
    }
}
