package joshie.progression.helpers;

import joshie.progression.api.criteria.ITriggerProvider;

public class TriggerHelper {
    public static int getInternalID(ITriggerProvider trigger) {
        for (int id = 0; id < trigger.getCriteria().getTriggers().size(); id++) {
            ITriggerProvider aTrigger = trigger.getCriteria().getTriggers().get(id);
            if (aTrigger.equals(trigger)) return id;
        }

        return 0;
    }
}
