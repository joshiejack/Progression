package joshie.progression.criteria.triggers;

import java.util.UUID;

public abstract class TriggerBaseAlwaysTrue extends TriggerBase {
    @Override
    public int getPercentage() {
        return 100;
    }

    @Override
    public boolean isCompleted() {
        return true;
    }

    @Override
    public boolean onFired(UUID uuid, Object... data) {
        return true;
    }
}