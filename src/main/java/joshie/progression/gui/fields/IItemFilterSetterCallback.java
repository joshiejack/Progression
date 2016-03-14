package joshie.progression.gui.fields;

import java.util.List;

import joshie.progression.api.IItemFilter;

public interface IItemFilterSetterCallback {
    public void setFilter(String fieldName, List<IItemFilter> filters);
}
