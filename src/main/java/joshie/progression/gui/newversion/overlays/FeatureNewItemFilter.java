package joshie.progression.gui.newversion.overlays;

import joshie.progression.api.IFilter;
import joshie.progression.gui.newversion.GuiItemFilterEditor;
import joshie.progression.handlers.APIHandler;

public class FeatureNewItemFilter extends FeatureNew {
    public static final FeatureNewItemFilter INSTANCE = new FeatureNewItemFilter();
    
    public FeatureNewItemFilter() {
        super("item");
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int button) {       
        int yPos = 0;
        int xPos = 0;
        for (IFilter filter : APIHandler.itemFilterTypes.values()) {
            if (!GuiItemFilterEditor.INSTANCE.field.isAccepted(filter)) continue;
            if (mouseX >= (xPos * 100) + 155 && mouseX <= (xPos * 100) + 255) {
                if (mouseY >= 46 + (yPos * 12) && mouseY < 46 + (yPos * 12) + 12) {
                    try {
                        APIHandler.cloneFilter(GuiItemFilterEditor.INSTANCE.field, filter);
                    } catch (Exception e) { e.printStackTrace(); }
                    
                    GuiItemFilterEditor.INSTANCE.initGui(); //Refresh the gui
                    setVisibility(false); //Hide this menu
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
        for (IFilter filter : APIHandler.itemFilterTypes.values()) {
            if (!GuiItemFilterEditor.INSTANCE.field.isAccepted(filter)) continue;
            int color = theme.newTriggerFont;
            if (mouseX >= (xPos * 100) + 155 && mouseX <= (xPos * 100) + 255) {
                if (mouseY >= 46 + (yPos * 12) && mouseY < 46 + (yPos * 12) + 12) {
                    color = theme.newTriggerFontHover;
                }
            }

            offset.drawText(filter.getLocalisedName(), (xPos * 100) + 155, 46 + (yPos * 12), color);

            xPos++;

            if (xPos > 1) {
                yPos++;
                xPos = 0;
            }
        }
    }
}
