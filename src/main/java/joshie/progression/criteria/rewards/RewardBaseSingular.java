package joshie.progression.criteria.rewards;

import joshie.progression.api.special.DisplayMode;
import joshie.progression.api.special.ICustomDescription;
import joshie.progression.api.special.ICustomWidth;

public abstract class RewardBaseSingular extends RewardBase implements ICustomDescription, ICustomWidth {
    @Override
    public int getWidth(DisplayMode mode) {
        return 100;
    }

    @Override
    public boolean shouldRunOnce() {
        return true;
    }
}
