package joshie.progression.criteria;

import joshie.progression.Progression;
import joshie.progression.api.IFieldProvider;
import joshie.progression.api.IRewardType;
import joshie.progression.gui.newversion.overlays.IDrawable;

public class Reward implements IDrawable {
    private Criteria criteria;
    private IRewardType reward;
    public boolean optional = false;
    private int ticker;

    public Reward(Criteria criteria, IRewardType reward, boolean optional) {
        this.criteria = criteria;
        this.reward = reward;
        this.optional = optional;
        this.reward.markCriteria(criteria);
    }

    public IRewardType getType() {
        return reward;
    }
    
    @Override
    public void update() {
        reward.update();
    }
    
    @Override
    public IFieldProvider getProvider() {
        return reward;
    }

    @Override
    public int getColor() {
        return reward.getColor();
    }

    @Override
    public String getLocalisedName() {
        return Progression.translate("reward." + reward.getUnlocalisedName());
    }

    @Override
    public String getDescription() {
        return reward.getDescription();
    }

    @Override
    public String getUnlocalisedName() {
        return reward.getUnlocalisedName();
    }
}
