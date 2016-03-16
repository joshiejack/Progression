package joshie.progression.helpers;

import joshie.progression.api.ITriggerType;

public class TriggerHelper {
    public static int getInternalID(ITriggerType trigger) {
        for (int id = 0; id < trigger.getCriteria().getTriggers().size(); id++) {
            ITriggerType aTrigger = trigger.getCriteria().getTriggers().get(id);
            if (aTrigger.equals(trigger)) return id;
        }

        return 0;
    }
}
