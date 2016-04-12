package joshie.progression.gui.editors;

import joshie.progression.api.criteria.IFilterProvider;
import joshie.progression.gui.fields.ItemFilterField;

import static joshie.progression.gui.core.GuiList.NEW_FILTER;
import static joshie.progression.gui.core.GuiList.THEME;

public class FeatureFilter extends FeatureDrawable<IFilterProvider> {
    public FeatureFilter() {
        super("filter", 45, NEW_FILTER, THEME.conditionGradient1, THEME.conditionGradient2, THEME.conditionFontColor, 0xFFF9F462);
    }

    public FeatureFilter setFilterField(ItemFilterField field) {
        setDrawable(field.getFilters());
        return this;
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
