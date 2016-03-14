package joshie.progression.helpers;

import java.util.Collection;
import java.util.Iterator;

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
}
