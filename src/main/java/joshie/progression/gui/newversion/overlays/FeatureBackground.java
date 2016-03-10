package joshie.progression.gui.newversion.overlays;

public class FeatureBackground extends FeatureAbstract {
	@Override
	public void drawFeature(int mouseX, int mouseY) {
		draw.drawRectangle(-1, 0, screenWidth + 1, guiHeight, theme.backgroundColor, theme.backgroundBorder);
	}
}
