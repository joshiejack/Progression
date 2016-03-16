package joshie.progression.criteria.triggers;

import java.util.ArrayList;
import java.util.List;

import joshie.progression.api.IFilter;

public abstract class TriggerBaseItemFilter extends TriggerBaseCounter {
    public List<IFilter> filters = new ArrayList();

    public TriggerBaseItemFilter(String name, int color) {
        super(name, color);
    }

    public TriggerBaseItemFilter(String name, int color, String data) {
        super(name, color, data);
    }
}
