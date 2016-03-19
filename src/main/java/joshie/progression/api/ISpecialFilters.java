package joshie.progression.api;

import joshie.progression.api.filters.IFilterSelectorFilter;

/** Implement this on things with item filters, to force them to only accept blocks **/
public interface ISpecialFilters {
    /** Return the special selector for this field **/
    public IFilterSelectorFilter getFilterForField(String fieldName);
}
