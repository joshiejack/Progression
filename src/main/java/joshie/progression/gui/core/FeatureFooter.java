package joshie.progression.gui.core;

import joshie.progression.Progression;

import static joshie.progression.gui.core.GuiList.THEME;

public class FeatureFooter extends FeatureAbstract {
    @Override
    public void drawFeature(int mouseX, int mouseY) {
        offset.drawRectangle(-1, 215, screenWidth, 1, THEME.blackBarUnderLineBorder, THEME.blackBarUnderLineBorder);
        offset.drawText(Progression.translate("footer.line1"), 9, 218, THEME.scrollTextFontColor);
        offset.drawText(Progression.translate("footer.line2"), 9, 228, THEME.scrollTextFontColor);
    }

    @Override
    public boolean isOverlay() {
        return false;
    }
}
