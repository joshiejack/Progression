package joshie.progression.helpers;

import joshie.progression.api.criteria.IRuleProvider;
import joshie.progression.handlers.EventsManager;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class CollectionHelper {
    public static boolean remove(Collection collection, Object object) {
        boolean removed = false;
        Iterator it = collection.iterator();
        while (it.hasNext()) {
            Object o = it.next();
            if (o.equals(object)) {
                removed = true;
                it.remove();
                break;
            }
        }

        return removed;
    }

    public static boolean removeAndUpdate(List drawable, IRuleProvider drawing) {
        EventsManager.onRemoved(drawing.getProvided());
        CollectionHelper.remove(drawable, drawing);
        return true;
    }

    public static void removeAll(Collection collection, List list) {
        for (Object object: list) {
            remove(collection, object);
        }
    }
}
