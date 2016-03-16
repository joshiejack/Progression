package joshie.progression.criteria;

import joshie.progression.api.IFieldProvider;
import joshie.progression.api.IRewardType;
import joshie.progression.gui.newversion.overlays.IDrawable;

public class Reward implements IDrawable {
    private Criteria criteria;
    private IRewardType reward;

    public Reward(Criteria criteria, IRewardType reward, boolean optional) {
        this.criteria = criteria;
        this.reward = reward;
        this.reward.markCriteria(criteria);
    }

    public IRewardType getType() {
        return reward;
    }

    @Override
    public IFieldProvider getProvider() {
        return reward;
    }
}
