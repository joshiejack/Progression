package joshie.progression.helpers;

import joshie.progression.api.criteria.IRuleProvider;
import joshie.progression.handlers.EventsManager;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class CollectionHelper {
    public static void remove(Collection collection, Object object) {
        Iterator it = collection.iterator();
        while (it.hasNext()) {
            Object o = it.next();
            if (o.equals(object)) {
                it.remove();
                break;
            }
        }
    }

    public static void removeAndUpdate(List<IRuleProvider> drawable, IRuleProvider drawing) {
        /*if (drawing instanceof IRewardProvider) {
            EventsManager.onRemoved(drawing.getProvided());
            CollectionHelper.remove(CRITERIA_EDITOR.getCriteria().getRewards(), drawing);
        } else if (drawing instanceof ITriggerProvider) {
            EventsManager.onRemoved(drawing.getProvided());
            CollectionHelper.remove(CRITERIA_EDITOR.getCriteria().getTriggers(), drawing);
        } else if (drawing instanceof IFilterProvider) {
            EventsManager.onRemoved(drawing.getProvided());
            FILTER_EDITOR.getField().remove((IFilterProvider) drawing);
        } else if (drawing instanceof IConditionProvider) {
            EventsManager.onRemoved(drawing.getProvided());
            CollectionHelper.remove(CONDITION_EDITOR.getTrigger().getConditions(), drawing);
        } */

        EventsManager.onRemoved(drawing.getProvided());
        CollectionHelper.remove(drawable, drawing);
    }

    public static void removeAll(Collection collection, List list) {
        for (Object object: list) {
            remove(collection, object);
        }
    }
}
