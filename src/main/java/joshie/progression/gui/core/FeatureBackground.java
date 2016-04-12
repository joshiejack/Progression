package joshie.progression.gui.core;

public class FeatureBackground extends FeatureAbstract {
	@Override
	public void drawFeature(int mouseX, int mouseY) {
		offset.drawRectangle(-5, 0, screenWidth + 10, guiHeight, GuiList.THEME.backgroundColor, GuiList.THEME.backgroundBorder);
	}

    @Override
    public boolean isOverlay() {
        return false;
    }
}
