package joshie.progression.criteria;

import java.util.List;

import joshie.progression.api.IRewardType;
import joshie.progression.gui.newversion.overlays.DrawFeatureHelper;
import joshie.progression.gui.newversion.overlays.IDrawable;
import joshie.progression.handlers.EventsManager;
import joshie.progression.helpers.ListHelper;
import joshie.progression.helpers.MCClientHelper;
import net.minecraftforge.fml.common.eventhandler.Event.Result;

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
    public void draw(DrawFeatureHelper helper, int renderX, int renderY, int mouseOffsetX, int mouseOffsetY) {
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
            reward.drawEditor(helper, renderX, renderY, mouseOffsetX, mouseOffsetY);
        } else {
            helper.drawSplitText(renderX, renderY, reward.getDescription(), 6, 20, 80, helper.getTheme().rewardBoxFont);
            reward.drawDisplay(mouseOffsetX, mouseOffsetY);
        }
        
    }

    @Override
    public boolean mouseClicked(int mouseOffsetX, int mouseOffsetY, int xPos, int button) {
        return reward.onClicked(mouseOffsetX, mouseOffsetY) != Result.DEFAULT;
    }

    @Override
    public void remove(List list) {
        EventsManager.onRewardRemoved(this);
        ListHelper.remove(list, this); //Remove from temporary list    
        ListHelper.remove(criteria.rewards, this); //Remove from real list
    }

    public void draw(int mouseX, int mouseY, int xPos) {
        /*
        this.mouseX = mouseX - xPosition;
        this.mouseY = mouseY - 140;
        this.xPosition = xPos + 6;
        DrawHelper.INSTANCE.setOffset(xPosition, 140);

        ProgressionAPI.draw.drawGradient(1, 2, 99, 15, reward.getColor(), Theme.INSTANCE.blackBarGradient1, Theme.INSTANCE.blackBarGradient2);
        ProgressionAPI.draw.drawText(reward.getLocalisedName(), 6, 6, Theme.INSTANCE.blackBarFontColor);

        if (MCClientHelper.canEdit()) {
            int xXcoord = 234;
            if (this.mouseX >= 87 && this.mouseX <= 97 && this.mouseY >= 4 && this.mouseY <= 14) {
                xXcoord += 11;
            }

            MCClientHelper.getMinecraft().getTextureManager().bindTexture(ProgressionInfo.textures);
            ProgressionAPI.draw.drawTexture(87, 4, xXcoord, 52, 11, 11);
        }

        getType().draw(this.mouseX, this.mouseY); */
    }
}
