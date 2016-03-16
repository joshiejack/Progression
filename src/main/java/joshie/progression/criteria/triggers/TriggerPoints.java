package joshie.progression.criteria.triggers;

import java.util.UUID;

import joshie.progression.api.EventBusType;
import joshie.progression.api.ITriggerData;
import joshie.progression.criteria.triggers.data.DataBoolean;
import joshie.progression.player.PlayerTracker;

public class TriggerPoints extends TriggerBaseBoolean {
    public String variable = "research";
    public double amount = 1D;
    public boolean consume = true;
    public boolean greaterThan = true;
    public boolean isEqualTo = true;
    public boolean lesserThan = false;

    public TriggerPoints() {
        super("points", 0xFFB2B200, EventBusType.NONE);
    }

    private boolean isValidValue(double total) {
        if (greaterThan && total > amount) return true;
        if (isEqualTo && total == amount) return true;
        if (lesserThan && total < amount) return true;

        //FALSE BABY!!!
        return false;
    }

    @Override
    public boolean onFired(UUID uuid, ITriggerData iTriggerData, Object... data) {
        double total = PlayerTracker.getServerPlayer(uuid).getAbilities().getPoints("points:" + variable);
        if (isValidValue(total)) {
            ((DataBoolean) iTriggerData).completed = true;
            if (consume) {
                PlayerTracker.getServerPlayer(uuid).addPoints(variable, -amount);
            }
        }

        return true;
    }
}
