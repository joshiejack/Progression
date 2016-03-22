package joshie.progression.gui.core;

import joshie.progression.Progression;

public class FeatureFooter extends FeatureAbstract {
	@Override
	public void drawFeature(int mouseX, int mouseY) {
	    offset.drawRectangle(-1, 215, screenWidth, 1, theme.blackBarUnderLineBorder, theme.blackBarUnderLineBorder);
		offset.drawText(Progression.translate("footer.line1"), 9, 218, theme.scrollTextFontColor);
        offset.drawText(Progression.translate("footer.line2"), 9, 228, theme.scrollTextFontColor);
	}

    @Override
    public boolean isOverlay() {
        return false;
    }
}
