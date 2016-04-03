package joshie.progression.gui.editors.insert;

import java.util.Collection;

import joshie.progression.api.criteria.IReward;
import joshie.progression.handlers.APIHandler;

public class FeatureNewReward extends FeatureNew<IReward> {
    public static final FeatureNewReward INSTANCE = new FeatureNewReward();

    public FeatureNewReward() {
        super("reward");
    }

    @Override
    public Collection<IReward> getFields() {
        return APIHandler.rewardTypes.values();
    }

    @Override
    public void clone(IReward reward) {
        APIHandler.cloneReward(criteria, reward);
        // GuiCriteriaEditor.INSTANCE.initGui(); //Refresh the gui
    }
}
