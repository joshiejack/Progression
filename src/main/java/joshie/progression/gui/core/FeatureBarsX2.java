package joshie.progression.gui.core;

import joshie.progression.Progression;

import static joshie.progression.gui.core.IBarProvider.BarColorType.*;

public class FeatureBarsX2 extends FeatureBarsX1 {
    protected String bar2;

    public FeatureBarsX2(String bar1, String bar2) {
        super(bar1);
        this.bar2 = bar2;
    }

    @Override
    public void drawFeature(int mouseX, int mouseY) {
        super.drawFeature(mouseX, mouseY);
        offset.drawGradient(-1, 120, screenWidth, 15, provider.getColorForBar(BAR2_GRADIENT1), provider.getColorForBar(BAR2_GRADIENT2), provider.getColorForBar(BAR2_BORDER));
        offset.drawText(Progression.translate(bar2), 9, 124, provider.getColorForBar(BAR2_FONT));
    }
}
