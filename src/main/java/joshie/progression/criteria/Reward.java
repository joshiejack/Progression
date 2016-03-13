package joshie.progression.criteria;

import java.util.List;

import joshie.progression.api.IField;
import joshie.progression.api.IRewardType;
import joshie.progression.gui.newversion.overlays.IDrawable;
import joshie.progression.handlers.EventsManager;
import joshie.progression.helpers.ListHelper;
import joshie.progression.json.Theme;

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
    public List<IField> getFields() {
        return reward.getFields();
    }

    @Override
    public void remove(List list) {
        EventsManager.onRewardRemoved(this);
        ListHelper.remove(list, this); //Remove from temporary list    
        ListHelper.remove(criteria.rewards, this); //Remove from real list
    }

    @Override
    public int getColor() {
        return reward.getColor();
    }

    @Override
    public int getGradient1() {
        return Theme.INSTANCE.rewardBoxGradient1;
    }

    @Override
    public int getGradient2() {
        return Theme.INSTANCE.rewardBoxGradient2;
    }

    @Override
    public int getFontColor() {
        return Theme.INSTANCE.rewardBoxFont;
    }

    @Override
    public String getLocalisedName() {
        return reward.getLocalisedName();
    }

    @Override
    public String getDescription() {
        return reward.getDescription();
    }

    @Override
    public void drawDisplay(int mouseX, int mouseY) {
        reward.drawDisplay(mouseX, mouseY);
    }
}
