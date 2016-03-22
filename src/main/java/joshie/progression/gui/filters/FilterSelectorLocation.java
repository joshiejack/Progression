package joshie.progression.gui.filters;

import java.util.ArrayList;
import java.util.List;

import joshie.progression.api.criteria.IProgressionFilterSelector;
import joshie.progression.api.criteria.IProgressionFilter.FilterType;
import joshie.progression.lib.WorldLocation;

public class FilterSelectorLocation extends FilterSelectorBase {
    public static final IProgressionFilterSelector INSTANCE = new FilterSelectorLocation();

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
