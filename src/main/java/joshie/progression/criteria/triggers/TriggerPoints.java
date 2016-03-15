package joshie.progression.criteria.triggers;

import java.util.UUID;

import joshie.progression.api.EventBusType;
import joshie.progression.api.ITriggerData;
import joshie.progression.criteria.triggers.data.DataBoolean;
import joshie.progression.player.PlayerTracker;

public class TriggerPoints extends TriggerBaseBoolean {
    public String name = "research";
    public int amount = 1;
    public boolean consume = true;

    public TriggerPoints() {
        super("points", 0xFFB2B200);
    }

    @Override
    public EventBusType getEventBus() {
        return EventBusType.NONE;
    }

    @Override
    public boolean onFired(UUID uuid, ITriggerData iTriggerData, Object... data) {
        int total = PlayerTracker.getServerPlayer(uuid).getAbilities().getPoints(name);
        if (total >= amount) {
            ((DataBoolean) iTriggerData).completed = true;
            if (consume) {
                PlayerTracker.getServerPlayer(uuid).addPoints(name, -amount);
            }
        }
        
        return true;
    }
}
