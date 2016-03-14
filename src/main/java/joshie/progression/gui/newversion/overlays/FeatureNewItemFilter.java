package joshie.progression.gui.newversion.overlays;

import joshie.progression.api.IItemFilter;
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
        for (IItemFilter filter : APIHandler.itemFilterTypes.values()) {
            if (mouseX >= (xPos * 100) + 155 && mouseX <= (xPos * 100) + 255) {
                if (mouseY >= 46 + (yPos * 12) && mouseY < 46 + (yPos * 12) + 12) {
                    try {
                        GuiItemFilterEditor.INSTANCE.field.add(filter.getClass().newInstance());
                    } catch (Exception e) { e.printStackTrace(); }
                    
                    GuiItemFilterEditor.INSTANCE.initGui(); //Refresh the gui
                    setVisibility(false); //Hide this menu
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
        for (IItemFilter filter : APIHandler.itemFilterTypes.values()) {
            int color = theme.newTriggerFont;
            if (mouseX >= (xPos * 100) + 155 && mouseX <= (xPos * 100) + 255) {
                if (mouseY >= 46 + (yPos * 12) && mouseY < 46 + (yPos * 12) + 12) {
                    color = theme.newTriggerFontHover;
                }
            }

            draw.drawText(filter.getLocalisedName(), (xPos * 100) + 155, 46 + (yPos * 12), color);

            yPos++;

            if (yPos > 6) {
                xPos++;
                yPos = 0;
            }
        }
    }
}
