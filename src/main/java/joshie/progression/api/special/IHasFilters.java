package joshie.progression.api.special;

import java.util.List;

import joshie.progression.api.criteria.IFilter;

public interface IHasFilters {
    public List<IFilter> getAllFilters();
}
