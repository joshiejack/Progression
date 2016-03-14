package joshie.progression.gui.newversion.overlays;

import joshie.progression.api.ITriggerType;
import joshie.progression.gui.newversion.GuiCriteriaEditor;
import joshie.progression.handlers.APIHandler;

public class FeatureNewTrigger extends FeatureNew {
    public static final FeatureNewTrigger INSTANCE = new FeatureNewTrigger();
    
    public FeatureNewTrigger() {
        super("trigger");
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int button) {
        int yPos = 0;
        int xPos = 0;
        for (ITriggerType trigger : APIHandler.triggerTypes.values()) {
            if (mouseX >= (xPos * 100) + 155 && mouseX <= (xPos * 100) + 255) {
                if (mouseY >= 46 + (yPos * 12) && mouseY < 46 + (yPos * 12) + 12) {
                    APIHandler.cloneTrigger(criteria, trigger);
                    GuiCriteriaEditor.INSTANCE.initGui(); //Refresh the gui
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
        for (ITriggerType trigger : APIHandler.triggerTypes.values()) {
            int color = theme.newTriggerFont;
            if (mouseX >= (xPos * 100) + 155 && mouseX <= (xPos * 100) + 255) {
                if (mouseY >= 46 + (yPos * 12) && mouseY < 46 + (yPos * 12) + 12) {
                    color = theme.newTriggerFontHover;
                }
            }

            offset.drawText(trigger.getLocalisedName(), (xPos * 100) + 155, 46 + (yPos * 12), color);

            yPos++;

            if (yPos > 6) {
                xPos++;
                yPos = 0;
            }
        }
    }
}
