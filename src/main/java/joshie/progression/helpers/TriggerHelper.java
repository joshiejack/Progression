package joshie.progression.helpers;

import joshie.progression.api.criteria.ITrigger;

public class TriggerHelper {
    public static int getInternalID(ITrigger trigger) {
        for (int id = 0; id < trigger.getCriteria().getTriggers().size(); id++) {
            ITrigger aTrigger = trigger.getCriteria().getTriggers().get(id);
            if (aTrigger.equals(trigger)) return id;
        }

        return 0;
    }
}
