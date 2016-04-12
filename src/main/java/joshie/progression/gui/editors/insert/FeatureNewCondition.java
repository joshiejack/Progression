package joshie.progression.gui.editors.insert;

import joshie.progression.api.criteria.IConditionProvider;
import joshie.progression.handlers.APIHandler;

import java.util.Collection;

import static joshie.progression.gui.core.GuiList.CONDITION_EDITOR;

public class FeatureNewCondition extends FeatureNew<IConditionProvider> {

    public FeatureNewCondition() {
        super("condition");
    }

    @Override
    public Collection<IConditionProvider> getFields() {
        return APIHandler.conditionTypes.values();
    }

    @Override
    public int getColor() {
        return CONDITION_EDITOR.get().getColor();
    }

    @Override
    public void clone(IConditionProvider provider) {
        APIHandler.cloneCondition(CONDITION_EDITOR.get(), provider);
    }
}
