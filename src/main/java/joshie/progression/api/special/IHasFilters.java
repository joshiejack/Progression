package joshie.progression.api.special;

import joshie.progression.api.criteria.IFilterProvider;
import joshie.progression.api.criteria.IFilterType;

import java.util.List;

public interface IHasFilters {
    /** Return all the filters in this object, combined as one,
     *  This is used to call IInit on them, so if you don't do this,
     *  then everything will break; */
    public List<IFilterProvider> getAllFilters();

    /** Return the special selector for this field **/
    public IFilterType getFilterForField(String fieldName);
}
