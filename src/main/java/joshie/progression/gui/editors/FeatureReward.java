package joshie.progression.gui.editors;

import joshie.progression.api.criteria.IProgressionCriteria;
import joshie.progression.api.criteria.IProgressionReward;
import joshie.progression.gui.editors.insert.FeatureNewReward;

public class FeatureReward extends FeatureDrawable<IProgressionReward> {
    public FeatureReward(IProgressionCriteria criteria) {
        super("reward", criteria.getRewards(), 140, FeatureNewReward.INSTANCE, theme.rewardBoxGradient1, theme.rewardBoxGradient2, theme.rewardBoxFont, theme.rewardBoxGradient2);
    }

    @Override
    public int drawSpecial(IProgressionReward drawing, int offsetX, int offsetY, int mouseOffsetX, int mouseOffsetY) {
        return super.drawSpecial(drawing, offsetX, offsetY, mouseOffsetX, mouseOffsetY);
    }

    @Override
    public boolean clickSpecial(IProgressionReward provider, int mouseOffsetX, int mouseOffsetY) {
        return false;
    }
}
