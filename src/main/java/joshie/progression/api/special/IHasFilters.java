package joshie.progression.api.special;

import java.util.List;

import joshie.progression.api.criteria.IProgressionFilter;

public interface IHasFilters {
    public List<IProgressionFilter> getAllFilters();
}
