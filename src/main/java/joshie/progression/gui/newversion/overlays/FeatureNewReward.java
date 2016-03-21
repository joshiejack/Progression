package joshie.progression.gui.newversion.overlays;

import java.util.Collection;

import joshie.progression.api.IRewardType;
import joshie.progression.handlers.APIHandler;

public class FeatureNewReward extends FeatureNew<IRewardType> {
    public static final FeatureNewReward INSTANCE = new FeatureNewReward();

    public FeatureNewReward() {
        super("reward");
    }

    @Override
    public Collection<IRewardType> getFields() {
        return APIHandler.rewardTypes.values();
    }

    @Override
    public void clone(IRewardType reward) {
        APIHandler.cloneReward(criteria, reward);
        // GuiCriteriaEditor.INSTANCE.initGui(); //Refresh the gui
    }
}
