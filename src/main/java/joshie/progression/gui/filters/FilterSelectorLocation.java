package joshie.progression.gui.filters;

import joshie.progression.api.criteria.IProgressionFilterSelector;
import joshie.progression.lib.WorldLocation;

import java.util.ArrayList;
import java.util.List;

public class FilterSelectorLocation extends FilterSelectorBase {
    public static final IProgressionFilterSelector INSTANCE = new FilterSelectorLocation();

    @Override
    public String getName() {
        return "location";
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
