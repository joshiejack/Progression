package joshie.progression.gui.editors.insert;

import joshie.progression.api.criteria.IConditionProvider;
import joshie.progression.gui.editors.GuiConditionEditor;
import joshie.progression.handlers.APIHandler;

import java.util.Collection;

public class FeatureNewCondition extends FeatureNew<IConditionProvider> {
    public static final FeatureNewCondition INSTANCE = new FeatureNewCondition();

    public FeatureNewCondition() {
        super("condition");
    }

    @Override
    public Collection<IConditionProvider> getFields() {
        return APIHandler.conditionTypes.values();
    }

    @Override
    public int getColor() {
        return GuiConditionEditor.INSTANCE.getTrigger().getColor();
    }

    @Override
    public void clone(IConditionProvider provider) {
        APIHandler.cloneCondition(GuiConditionEditor.INSTANCE.getTrigger(), provider);
    }
}
