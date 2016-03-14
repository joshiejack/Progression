package joshie.progression.gui.newversion.overlays;

import joshie.progression.api.IRewardType;
import joshie.progression.gui.newversion.GuiCriteriaEditor;
import joshie.progression.handlers.APIHandler;

public class FeatureNewReward extends FeatureNew {
    public static final FeatureNewReward INSTANCE = new FeatureNewReward();

    public FeatureNewReward() {
        super("reward");
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int button) {
        int yPos = 0;
        int xPos = 0;
        for (IRewardType reward : APIHandler.rewardTypes.values()) {
            if (mouseX >= (xPos * 100) + 155 && mouseX <= (xPos * 100) + 255) {
                if (mouseY >= 46 + (yPos * 12) && mouseY < 46 + (yPos * 12) + 12) {
                    IS_OPEN = false;
                    APIHandler.cloneReward(criteria, reward);
                    GuiCriteriaEditor.INSTANCE.initGui(); //Refresh the gui
                    setVisibility(false);
                    return true;
                }
            }

            xPos++;

            if (xPos > 1) {
                yPos++;
                xPos = 0;
            }
        }

        return false;
    }

    @Override
    public void drawForeground(int mouseX, int mouseY) {
        int yPos = 0;
        int xPos = 0;
        for (IRewardType reward : APIHandler.rewardTypes.values()) {
            int color = theme.newRewardFont;
            if (mouseX >= (xPos * 100) + 155 && mouseX <= (xPos * 100) + 255) {
                if (mouseY >= 46 + (yPos * 12) && mouseY < 46 + (yPos * 12) + 12) {
                    color = theme.newRewardFontHover;
                }
            }

            offset.drawText(reward.getLocalisedName(), (xPos * 100) + 155, 46 + (yPos * 12), color);

            xPos++;

            if (xPos > 1) {
                yPos++;
                xPos = 0;
            }
        }
    }
}
