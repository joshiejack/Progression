package joshie.progression.api.special;

import joshie.progression.api.criteria.IFilterType;

/** Implement this on things with item filters, to force them to only accept blocks **/
public interface ISpecialFilters {
    /** Return the special selector for this field **/
    public IFilterType getFilterForField(String fieldName);
}
