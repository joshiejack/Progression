package joshie.progression.criteria.filters.location;

import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.criteria.IFilterType;
import joshie.progression.criteria.filters.FilterBase;
import joshie.progression.lib.WorldLocation;

public abstract class FilterLocationBase extends FilterBase {
    @Override
    public boolean matches(Object object) {
        if (!(object instanceof WorldLocation)) return false;
        WorldLocation location = ((WorldLocation)object);
        if (location.player == null) return false;
        return matches(location);
    }

    @Override
    public IFilterType getType() {
        return ProgressionAPI.filters.getLocationFilter();
    }

    public boolean matches(WorldLocation location) {
        return location.equals(getRandom(location.player));
    }

    public static enum LocationOperator {
        THISORMORE, THISORLESS, RADIUS;
    }
}
