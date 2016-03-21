package joshie.progression.criteria.filters.location;

import java.util.ArrayList;
import java.util.List;

import joshie.progression.criteria.filters.FilterBase;
import joshie.progression.lib.WorldLocation;

public abstract class FilterLocationBase extends FilterBase {
    public FilterLocationBase(String name, int color) {
        super(name, color);
    }

    @Override
    public List<WorldLocation> getMatches(Object object) {
        return getMatches();
    }
    
    public List<WorldLocation> getMatches() {
        return new ArrayList();
    }

    @Override
    public boolean matches(Object object) {
        return object instanceof WorldLocation ? matches((WorldLocation) object) : false;
    }

    @Override
    public FilterType getType() {
        return FilterType.LOCATION;
    }

    public boolean matches(WorldLocation location) {
        return true;
    }
}
