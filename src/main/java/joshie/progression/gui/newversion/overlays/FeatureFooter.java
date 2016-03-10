package joshie.progression.gui.newversion.overlays;

import joshie.progression.Progression;

public class FeatureFooter extends FeatureAbstract {
	@Override
	public void drawFeature(int mouseX, int mouseY) {
		draw.drawRectangle(-1, 215, screenWidth, 1, theme.blackBarUnderLineBorder, theme.blackBarUnderLineBorder);
        draw.drawText(Progression.translate("footer.line1"), 9 - offsetX, 218, theme.scrollTextFontColor);
        draw.drawText(Progression.translate("footer.line2"), 9 - offsetX, 228, theme.scrollTextFontColor);
	}
}
