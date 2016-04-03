package joshie.progression.gui.editors;

import joshie.progression.api.criteria.ICriteria;
import joshie.progression.api.criteria.IReward;
import joshie.progression.gui.editors.insert.FeatureNewReward;

public class FeatureReward extends FeatureDrawable<IReward> {
    public FeatureReward(ICriteria criteria) {
        super("reward", criteria.getRewards(), 140, FeatureNewReward.INSTANCE, theme.rewardBoxGradient1, theme.rewardBoxGradient2, theme.rewardBoxFont, theme.rewardBoxGradient2);
    }

    @Override
    public int drawSpecial(IReward drawing, int offsetX, int offsetY, int mouseOffsetX, int mouseOffsetY) {
        return super.drawSpecial(drawing, offsetX, offsetY, mouseOffsetX, mouseOffsetY);
    }

    @Override
    public boolean clickSpecial(IReward provider, int mouseOffsetX, int mouseOffsetY) {
        return false;
    }
}
