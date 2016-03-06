package joshie.progression.gui;

import org.lwjgl.opengl.GL11;

import joshie.progression.api.IRewardType;
import joshie.progression.criteria.Criteria;
import joshie.progression.gui.base.OverlayBase;
import joshie.progression.handlers.APIHandler;

public class NewReward extends OverlayBase {
    public static NewReward INSTANCE = new NewReward();
    private static Criteria criteria = null;

    public void select(Criteria criteria) {
        if (reset()) {
            NewReward.criteria = criteria;
        }
    }

    @Override
    public void clear() {
        criteria = null;
    }

    @Override
    public boolean isVisible() {
        return criteria != null;
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int button) {
        int yPos = 0;
        int xPos = 0;
        for (IRewardType reward : APIHandler.rewardTypes.values()) {
            if (mouseX >= (xPos * 100) + 155 && mouseX <= (xPos * 100) + 255) {
                if (mouseY >= 46 + (yPos * 12) && mouseY < 46 + (yPos * 12) + 12) {
                    APIHandler.cloneReward(criteria, reward);
                    clear();
                    return true;
                }
            }

            yPos++;

            if (yPos > 8) {
                xPos++;
                yPos = 0;
            }
        }

        return false;
    }

    @Override
    public void draw(int x, int y) {
        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
        int mouseX = GuiCriteriaEditor.INSTANCE.mouseX;
        int mouseY = GuiCriteriaEditor.INSTANCE.mouseY;
        drawBox(-GuiCriteriaEditor.INSTANCE.offsetX + 150, 30, 200, 125, theme.newBox1, theme.newBox2);
        drawGradient(-GuiCriteriaEditor.INSTANCE.offsetX + 150, 30, 200, 15, theme.newRewardGradient1, theme.newRewardGradient2, theme.newRewardBorder);
        drawText("Select a Type of Reward", -GuiCriteriaEditor.INSTANCE.offsetX + 155, 34, theme.newRewardFont);
        int yPos = 0;
        int xPos = 0;
        for (IRewardType reward : APIHandler.rewardTypes.values()) {
            int color = theme.newRewardFont;
            if (mouseX >= (xPos * 100) + 155 && mouseX <= (xPos * 100) + 255) {
                if (mouseY >= 46 + (yPos * 12) && mouseY < 46 + (yPos * 12) + 12) {
                    color = theme.newRewardFontHover;
                }
            }

            drawText(reward.getLocalisedName(), -GuiCriteriaEditor.INSTANCE.offsetX + (xPos * 100) + 155, 46 + (yPos * 12), color);

            yPos++;

            if (yPos > 8) {
                xPos++;
                yPos = 0;
            }
        }
    }
}
