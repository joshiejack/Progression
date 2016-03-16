package joshie.progression.gui.newversion.overlays;

import joshie.progression.api.IConditionType;
import joshie.progression.gui.newversion.GuiConditionEditor;
import joshie.progression.handlers.APIHandler;

public class FeatureNewCondition extends FeatureNew {
    public static final FeatureNewCondition INSTANCE = new FeatureNewCondition();

    public FeatureNewCondition() {
        super("condition");
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int button) {
        int yPos = 0;
        int xPos = 0;
        for (IConditionType condition : APIHandler.conditionTypes.values()) {
            if (mouseX >= (xPos * 100) + 155 && mouseX <= (xPos * 100) + 255) {
                if (mouseY >= 46 + (yPos * 12) && mouseY < 46 + (yPos * 12) + 12) {
                    APIHandler.cloneCondition(trigger, condition);
                    GuiConditionEditor.INSTANCE.initGui(); //Refresh the gui
                    setVisibility(false);
                    return true;
                }
            }

            yPos++;

            if (yPos > 6) {
                xPos++;
                yPos = 0;
            }
        }

        return false;
    }

    @Override
    public void drawForeground(int mouseX, int mouseY) {
        int yPos = 0;
        int xPos = 0;
        for (IConditionType condition : APIHandler.conditionTypes.values()) {
            int color = theme.newConditionFont;
            if (mouseX >= (xPos * 100) + 155 && mouseX <= (xPos * 100) + 255) {
                if (mouseY >= 46 + (yPos * 12) && mouseY < 46 + (yPos * 12) + 12) {
                    color = theme.newConditionFontHover;
                }
            }

            offset.drawText(condition.getLocalisedName(), (xPos * 100) + 155, 46 + (yPos * 12), color);

            yPos++;

            if (yPos > 6) {
                xPos++;
                yPos = 0;
            }
        }
    }
}
