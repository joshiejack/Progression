package joshie.progression.criteria;

import joshie.progression.api.IRewardType;
import joshie.progression.api.ProgressionAPI;
import joshie.progression.gui.GuiDrawHelper;
import joshie.progression.helpers.ClientHelper;
import joshie.progression.json.Theme;
import joshie.progression.lib.ProgressionInfo;
import cpw.mods.fml.common.eventhandler.Event.Result;

public class Reward {
    private Criteria criteria;
    private IRewardType reward;
    public boolean optional = false;

    public Reward(Criteria criteria, IRewardType reward, boolean optional) {
        this.criteria = criteria;
        this.reward = reward;
        this.optional = optional;
    }

    public IRewardType getType() {
        return reward;
    }

    protected int xPosition;
    protected int mouseX;
    protected int mouseY;

    public Result onClicked() {
        if (ClientHelper.canEdit()) {
            if (this.mouseX >= 88 && this.mouseX <= 95 && this.mouseY >= 4 && this.mouseY <= 14) {
                return Result.DENY; //Delete this reward
            }
        }

        return ClientHelper.canEdit() ? reward.onClicked(mouseX, mouseY) : Result.DEFAULT;
    }

    public void draw(int mouseX, int mouseY, int xPos) {
        this.mouseX = mouseX - xPosition;
        this.mouseY = mouseY - 140;
        this.xPosition = xPos + 6;
        GuiDrawHelper.INSTANCE.setOffset(xPosition, 140);

        ProgressionAPI.draw.drawGradient(1, 2, 99, 15, reward.getColor(), Theme.INSTANCE.blackBarGradient1, Theme.INSTANCE.blackBarGradient2);
        ProgressionAPI.draw.drawText(reward.getLocalisedName(), 6, 6, Theme.INSTANCE.blackBarFontColor);

        if (ClientHelper.canEdit()) {
            int xXcoord = 234;
            if (this.mouseX >= 87 && this.mouseX <= 97 && this.mouseY >= 4 && this.mouseY <= 14) {
                xXcoord += 11;
            }

            ClientHelper.getMinecraft().getTextureManager().bindTexture(ProgressionInfo.textures);
            ProgressionAPI.draw.drawTexture(87, 4, xXcoord, 52, 11, 11);
        }

        getType().draw(this.mouseX, this.mouseY);
    }
}
