package joshie.crafting;

import joshie.crafting.api.DrawHelper;
import joshie.crafting.api.IRewardType;
import joshie.crafting.gui.GuiDrawHelper;
import joshie.crafting.helpers.ClientHelper;
import joshie.crafting.json.Theme;
import joshie.crafting.lib.CraftingInfo;
import cpw.mods.fml.common.eventhandler.Event.Result;

public class Reward {
    private Criteria criteria;
    private IRewardType reward;

    public Reward(Criteria criteria, IRewardType reward) {
        this.criteria = criteria;
        this.reward = reward;
    }

    public IRewardType getType() {
        return reward;
    }

    public Reward setCriteria(Criteria criteria) {
        this.criteria = criteria;
        return this;
    }

    public Criteria getCriteria() {
        return this.criteria;
    }

    protected int xPosition;
    protected int mouseX;
    protected int mouseY;

    protected void draw() {

    }

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
        GuiDrawHelper.TriggerDrawHelper.INSTANCE.setOffset(xPosition, 140);

        DrawHelper.triggerDraw.drawGradient(1, 2, 99, 15, reward.getColor(), Theme.INSTANCE.blackBarGradient1, Theme.INSTANCE.blackBarGradient2);
        DrawHelper.triggerDraw.drawText(reward.getLocalisedName(), 6, 6, Theme.INSTANCE.blackBarFontColor);

        if (ClientHelper.canEdit()) {
            int xXcoord = 0;
            if (this.mouseX >= 87 && this.mouseX <= 97 && this.mouseY >= 4 && this.mouseY <= 14) {
                xXcoord = 11;
            }

            ClientHelper.getMinecraft().getTextureManager().bindTexture(CraftingInfo.textures);
            DrawHelper.triggerDraw.drawTexture(87, 4, xXcoord, 195, 11, 11);
        }

        getType().draw(mouseX, mouseY);
    }
}
