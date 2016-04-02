package joshie.progression.gui.editors;

import joshie.progression.api.criteria.IProgressionFilter;
import joshie.progression.gui.editors.insert.FeatureNewItemFilter;
import joshie.progression.gui.fields.ItemFilterField;

public class FeatureFilter extends FeatureDrawable<IProgressionFilter> {
    public FeatureFilter(ItemFilterField field) {
        super("filter", field.getFilters(), 45, FeatureNewItemFilter.INSTANCE, theme.conditionGradient1, theme.conditionGradient2, theme.conditionFontColor, 0xFFF9F462);
    }

    @Override
    public int drawSpecial(IProgressionFilter drawing, int offsetX, int offsetY, int mouseOffsetX, int mouseOffsetY) {
        return super.drawSpecial(drawing, offsetX, offsetY, mouseOffsetX, mouseOffsetY);
    }

    @Override
    public boolean clickSpecial(IProgressionFilter provider, int mouseOffsetX, int mouseOffsetY) {
        return false;
    }
}
