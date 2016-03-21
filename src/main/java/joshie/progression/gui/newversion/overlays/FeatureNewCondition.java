package joshie.progression.gui.newversion.overlays;

import java.util.Collection;

import joshie.progression.api.IConditionType;
import joshie.progression.gui.newversion.GuiConditionEditor;
import joshie.progression.handlers.APIHandler;

public class FeatureNewCondition extends FeatureNew<IConditionType> {
    public static final FeatureNewCondition INSTANCE = new FeatureNewCondition();

    public FeatureNewCondition() {
        super("condition");
    }

    @Override
    public Collection<IConditionType> getFields() {
        return APIHandler.conditionTypes.values();
    }

    @Override
    public void clone(IConditionType provider) {
        APIHandler.cloneCondition(trigger, provider);
       // GuiConditionEditor.INSTANCE.initGui(); //Refresh the gui
    }
}
