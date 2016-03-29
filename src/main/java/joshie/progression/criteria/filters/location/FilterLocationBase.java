package joshie.progression.criteria.filters.location;

import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.criteria.IProgressionFilterSelector;
import joshie.progression.criteria.filters.FilterBase;
import joshie.progression.lib.WorldLocation;

public abstract class FilterLocationBase extends FilterBase {
    public FilterLocationBase(String name, int color) {
        super(name, color);
    }

    @Override
    public boolean matches(Object object) {
        return object instanceof WorldLocation ? matches((WorldLocation) object) : false;
    }

    @Override
    public IProgressionFilterSelector getType() {
        return ProgressionAPI.filters.getLocationFilter();
    }

    public boolean matches(WorldLocation location) {
        return true;
    }
}
