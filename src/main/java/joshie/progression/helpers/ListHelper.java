package joshie.progression.helpers;

import java.util.Iterator;
import java.util.List;

public class ListHelper {
    public static void remove(List list, Object object) {
        Iterator it = list.iterator();
        while (it.hasNext()) {
            Object o = it.next();
            if (o.equals(object)) {
                it.remove();
                break;
            }
        }
    }
}
