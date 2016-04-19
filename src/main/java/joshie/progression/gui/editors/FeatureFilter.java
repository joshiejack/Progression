package joshie.progression.gui.editors;

import joshie.progression.api.criteria.IFilterProvider;

import java.util.List;

import static joshie.progression.gui.core.GuiList.*;

public class FeatureFilter extends FeatureDrawable<IFilterProvider> {
    public FeatureFilter() {
        super("filter", 45, NEW_FILTER, THEME.conditionGradient1, THEME.conditionGradient2, THEME.conditionFontColor, 0xFFF9F462);
    }

    @Override
    public List<IFilterProvider> getList() {
        return FILTER_EDITOR.get().getFilters();
    }
}
