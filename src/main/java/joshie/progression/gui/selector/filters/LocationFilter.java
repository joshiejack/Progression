package joshie.progression.gui.selector.filters;

import java.util.ArrayList;
import java.util.List;

import joshie.progression.api.IFilter.FilterType;
import joshie.progression.api.filters.IFilterSelectorFilter;
import joshie.progression.lib.WorldLocation;

public class LocationFilter extends FilterBase {
    public static final IFilterSelectorFilter INSTANCE = new LocationFilter();

    @Override
    public FilterType getType() {
        return FilterType.LOCATION;
    }
    
    @Override
    public boolean isAcceptable(Object object) {
        return object instanceof WorldLocation;
    }

    @Override
    public List<WorldLocation> getAllItems() {
        return new ArrayList();
    }
}
