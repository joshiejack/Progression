package joshie.progression.gui.editors.insert;

import java.util.Collection;

import joshie.progression.api.criteria.IProgressionReward;
import joshie.progression.handlers.APIHandler;

public class FeatureNewReward extends FeatureNew<IProgressionReward> {
    public static final FeatureNewReward INSTANCE = new FeatureNewReward();

    public FeatureNewReward() {
        super("reward");
    }

    @Override
    public Collection<IProgressionReward> getFields() {
        return APIHandler.rewardTypes.values();
    }

    @Override
    public void clone(IProgressionReward reward) {
        APIHandler.cloneReward(criteria, reward);
        // GuiCriteriaEditor.INSTANCE.initGui(); //Refresh the gui
    }
}
