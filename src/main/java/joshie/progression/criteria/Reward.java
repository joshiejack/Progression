package joshie.progression.criteria;

import java.util.List;

import joshie.progression.api.IField;
import joshie.progression.api.IRewardType;
import joshie.progression.gui.newversion.overlays.DrawFeatureHelper;
import joshie.progression.gui.newversion.overlays.IDrawable;
import joshie.progression.handlers.EventsManager;
import joshie.progression.helpers.ListHelper;
import joshie.progression.helpers.MCClientHelper;
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
    public void draw(DrawFeatureHelper helper, int renderX, int renderY, int mouseX, int mouseY) {
        //For updating the render ticker
        ticker++;
        if (ticker == 0 || ticker >= 200) {
            reward.update();
            ticker = 1;
        }

        int width = MCClientHelper.isInEditMode() ? 99 : 79;
        helper.drawGradient(renderX, renderY, 1, 2, width, 15, getType().getColor(), helper.getTheme().rewardBoxGradient1, helper.getTheme().rewardBoxGradient2);
        helper.drawText(renderX, renderY, getType().getLocalisedName(), 6, 6, helper.getTheme().rewardBoxFont);
        if (MCClientHelper.isInEditMode()) {
            int index = 0;
            for (IField t : reward.getFields()) {
                int color = Theme.INSTANCE.optionsFontColor;
                int yPos = 17 + (index * 8);
                if (MCClientHelper.canEdit()) {
                    if (mouseX >= 1 && mouseX <= 84) {
                        if (mouseY >= yPos && mouseY < yPos + 8) {
                            color = Theme.INSTANCE.optionsFontColorHover;
                        }
                    }
                }

                t.draw(helper, renderX, renderY, color, yPos);
                index++;
            }
            
            //Incase you want additional shizz
            reward.drawEditor(helper, renderX, renderY, mouseX, mouseY);
        } else {
            helper.drawSplitText(renderX, renderY, reward.getDescription(), 6, 20, 80, helper.getTheme().rewardBoxFont);
            reward.drawDisplay(mouseX, mouseY);
        }

    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int xPos, int button) {
        if (MCClientHelper.canEdit()) {
            int index = 0;
            for (IField t : reward.getFields()) {
                int color = Theme.INSTANCE.optionsFontColor;
                int yPos = 17 + (index * 8);
                if (mouseX >= 1 && mouseX <= 84) {
                    if (mouseY >= yPos && mouseY < yPos + 8) {
                        t.click();
                        return true;
                    }
                }

                if (t.attemptClick(mouseX, mouseY)) {
                    return true;
                }

                index++;
            }
        }

        return false;
    }

    @Override
    public void remove(List list) {
        EventsManager.onRewardRemoved(this);
        ListHelper.remove(list, this); //Remove from temporary list    
        ListHelper.remove(criteria.rewards, this); //Remove from real list
    }
}
