package joshie.progression.gui.newversion.overlays;

public class FeatureBackground extends FeatureAbstract {
	@Override
	public void drawFeature(int mouseX, int mouseY) {
		offset.drawRectangle(-5, 0, screenWidth + 10, guiHeight, theme.backgroundColor, theme.backgroundBorder);
	}

    @Override
    public boolean isOverlay() {
        return false;
    }
}
