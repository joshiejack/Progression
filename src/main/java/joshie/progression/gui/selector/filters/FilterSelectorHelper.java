package joshie.progression.gui.selector.filters;

import joshie.progression.api.filters.IFilterSelectorFilter;
import joshie.progression.api.filters.IFilterHelper;

public class FilterSelectorHelper implements IFilterHelper {
    @Override
    public IFilterSelectorFilter getBlockFilter() {
        return BlockFilter.INSTANCE;
    }

    @Override
    public IFilterSelectorFilter getEntityFilter() {
        return EntityFilter.INSTANCE;
    }

    @Override
    public IFilterSelectorFilter getPotionFilter() {
        return PotionFilter.INSTANCE;
    }

    @Override
    public IFilterSelectorFilter getLocationFilter() {
        return LocationFilter.INSTANCE;
    }

    @Override
    public IFilterSelectorFilter getItemStackFilter() {
        return ItemFilter.INSTANCE;
    }
}
