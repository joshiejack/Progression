package joshie.progression.helpers;

import joshie.progression.api.criteria.IProgressionTrigger;

public class TriggerHelper {
    public static int getInternalID(IProgressionTrigger trigger) {
        for (int id = 0; id < trigger.getCriteria().getTriggers().size(); id++) {
            IProgressionTrigger aTrigger = trigger.getCriteria().getTriggers().get(id);
            if (aTrigger.equals(trigger)) return id;
        }

        return 0;
    }
}
