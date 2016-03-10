package joshie.progression.gui.newversion.overlays;

import static joshie.progression.gui.newversion.overlays.IBarProvider.BarColorType.BAR2_BORDER;
import static joshie.progression.gui.newversion.overlays.IBarProvider.BarColorType.BAR2_FONT;
import static joshie.progression.gui.newversion.overlays.IBarProvider.BarColorType.BAR2_GRADIENT1;
import static joshie.progression.gui.newversion.overlays.IBarProvider.BarColorType.BAR2_GRADIENT2;

import joshie.progression.Progression;

public class FeatureBarsX2 extends FeatureBarsX1 {
	private String bar2;
	
	public FeatureBarsX2(IBarProvider provider, String bar1, String bar2) {
		super(provider, bar1);
		this.bar2 = bar2;
	}

	@Override
	public void drawFeature(int mouseX, int mouseY) {
		super.drawFeature(mouseX, mouseY);
		offset.drawGradient(-1, 120, screenWidth, 15, provider.getColorForBar(BAR2_GRADIENT1), provider.getColorForBar(BAR2_GRADIENT2), provider.getColorForBar(BAR2_BORDER));
        offset.drawText(Progression.translate(bar2), 9, 124, provider.getColorForBar(BAR2_FONT));
	}
}
