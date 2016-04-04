package joshie.progression.gui.editors;

import joshie.progression.api.criteria.IFilterProvider;
import joshie.progression.gui.editors.insert.FeatureNewItemFilter;
import joshie.progression.gui.fields.ItemFilterField;

public class FeatureFilter extends FeatureDrawable<IFilterProvider> {
    public FeatureFilter(ItemFilterField field) {
        super("filter", field.getFilters(), 45, FeatureNewItemFilter.INSTANCE, theme.conditionGradient1, theme.conditionGradient2, theme.conditionFontColor, 0xFFF9F462);
    }

    @Override
    public int drawSpecial(IFilterProvider drawing, int offsetX, int offsetY, int mouseOffsetX, int mouseOffsetY) {
        return super.drawSpecial(drawing, offsetX, offsetY, mouseOffsetX, mouseOffsetY);
    }

    @Override
    public boolean clickSpecial(IFilterProvider provider, int mouseOffsetX, int mouseOffsetY) {
        return false;
    }
}
