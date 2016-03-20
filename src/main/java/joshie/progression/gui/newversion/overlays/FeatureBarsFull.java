package joshie.progression.gui.newversion.overlays;

import static joshie.progression.gui.newversion.overlays.IBarProvider.BarColorType.BAR1_BORDER;
import static joshie.progression.gui.newversion.overlays.IBarProvider.BarColorType.BAR1_FONT;
import static joshie.progression.gui.newversion.overlays.IBarProvider.BarColorType.BAR1_GRADIENT1;
import static joshie.progression.gui.newversion.overlays.IBarProvider.BarColorType.BAR1_GRADIENT2;
import static joshie.progression.gui.newversion.overlays.IBarProvider.BarColorType.BAR1_UNDERLINE;

import joshie.progression.Progression;

public class FeatureBarsFull extends FeatureAbstract {
    protected IBarProvider provider;
    private String bar1;
    
    public FeatureBarsFull(IBarProvider provider, String bar1) {
        this.provider = provider;
        this.bar1 = bar1;
    }

    @Override
    public void drawFeature(int mouseX, int mouseY) {
        offset.drawGradient(-1, 5, screenWidth, 15, provider.getColorForBar(BAR1_GRADIENT1), provider.getColorForBar(BAR1_GRADIENT2), provider.getColorForBar(BAR1_BORDER));
        offset.drawRectangle(-1, 20, screenWidth, 1, provider.getColorForBar(BAR1_UNDERLINE), theme.invisible);
        offset.drawText(Progression.translate(bar1), 9, 9, provider.getColorForBar(BAR1_FONT)); //Removing the offsetX in order to reposition everything back at 0
    }

    @Override
    public boolean isOverlay() {
        return false;
    }
}
