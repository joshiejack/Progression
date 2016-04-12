package joshie.progression.gui.editors.insert;

import joshie.progression.api.criteria.IRewardProvider;
import joshie.progression.handlers.APIHandler;

import java.util.Collection;

import static joshie.progression.gui.core.GuiList.CRITERIA_EDITOR;

public class FeatureNewReward extends FeatureNew<IRewardProvider> {

    public FeatureNewReward() {
        super("reward");
    }

    @Override
    public Collection<IRewardProvider> getFields() {
        return APIHandler.rewardTypes.values();
    }

    @Override
    public void clone(IRewardProvider reward) {
        APIHandler.cloneReward(CRITERIA_EDITOR.get(), reward);
    }
}
