package joshie.progression.gui.editors;

import joshie.progression.api.criteria.IProgressionReward;
import joshie.progression.gui.core.IGuiFeature;

import java.util.List;

public class FeatureReward extends FeatureDrawable<IProgressionReward> {
    public FeatureReward(String text, List drawable, int offsetY, int x1, int x2, int y1, int y2, IGuiFeature newDrawable, int gradient1, int gradient2, int fontColor) {
        super(text, drawable, offsetY, x1, x2, y1, y2, newDrawable, gradient1, gradient2, fontColor);
    }

    @Override
    public int drawSpecial(IProgressionReward drawing, int offsetX, int offsetY, int mouseOffsetX, int mouseOffsetY) {
        return super.drawSpecial(drawing, offsetX, offsetY, mouseOffsetX, mouseOffsetY);
    }

    @Override
    public boolean clickSpecial(IProgressionReward provider, int mouseOffsetX, int mouseOffsetY) {
        return false;
    }
}
