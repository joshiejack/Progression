package joshie.progression.gui.editors.insert;

import joshie.progression.api.criteria.IRewardProvider;
import joshie.progression.gui.editors.GuiCriteriaEditor;
import joshie.progression.handlers.APIHandler;

import java.util.Collection;

public class FeatureNewReward extends FeatureNew<IRewardProvider> {
    public static final FeatureNewReward INSTANCE = new FeatureNewReward();

    public FeatureNewReward() {
        super("reward");
    }

    @Override
    public Collection<IRewardProvider> getFields() {
        return APIHandler.rewardTypes.values();
    }

    @Override
    public void clone(IRewardProvider reward) {
        APIHandler.cloneReward(GuiCriteriaEditor.INSTANCE.getCriteria(), reward);
    }
}
