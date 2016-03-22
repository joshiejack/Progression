package joshie.progression.api.special;

import joshie.progression.api.criteria.IProgressionFilterSelector;

/** Implement this on things with item filters, to force them to only accept blocks **/
public interface ISpecialFilters {
    /** Return the special selector for this field **/
    public IProgressionFilterSelector getFilterForField(String fieldName);
}
