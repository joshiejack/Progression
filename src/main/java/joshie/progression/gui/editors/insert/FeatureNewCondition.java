package joshie.progression.gui.editors.insert;

import java.util.Collection;

import joshie.progression.api.criteria.IProgressionCondition;
import joshie.progression.gui.editors.GuiConditionEditor;
import joshie.progression.handlers.APIHandler;

public class FeatureNewCondition extends FeatureNew<IProgressionCondition> {
    public static final FeatureNewCondition INSTANCE = new FeatureNewCondition();

    public FeatureNewCondition() {
        super("condition");
    }

    @Override
    public Collection<IProgressionCondition> getFields() {
        return APIHandler.conditionTypes.values();
    }

    @Override
    public void clone(IProgressionCondition provider) {
        APIHandler.cloneCondition(trigger, provider);
       // GuiConditionEditor.INSTANCE.initGui(); //Refresh the gui
    }
}
