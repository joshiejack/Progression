package joshie.progression.gui.editors.insert;

import joshie.progression.api.criteria.ICondition;
import joshie.progression.gui.editors.GuiConditionEditor;
import joshie.progression.handlers.APIHandler;

import java.util.Collection;

public class FeatureNewCondition extends FeatureNew<ICondition> {
    public static final FeatureNewCondition INSTANCE = new FeatureNewCondition();

    public FeatureNewCondition() {
        super("condition");
    }

    @Override
    public Collection<ICondition> getFields() {
        return APIHandler.conditionTypes.values();
    }

    @Override
    public int getColor() {
        return GuiConditionEditor.INSTANCE.getTrigger().getColor();
    }

    @Override
    public void clone(ICondition provider) {
        APIHandler.cloneCondition(trigger, provider);
       // GuiConditionEditor.INSTANCE.initGui(); //Refresh the gui
    }
}
