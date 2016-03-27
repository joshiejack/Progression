package joshie.progression.criteria.triggers;

import joshie.progression.api.ProgressionAPI;
import joshie.progression.items.ItemCriteria;
import joshie.progression.player.PlayerTracker;
import net.minecraftforge.fml.common.eventhandler.EventBus;

import java.util.UUID;

public class TriggerPoints extends TriggerBaseBoolean {
    public String variable = "gold";
    public double amount = 1D;
    public boolean consume = true;
    public boolean greaterThan = true;
    public boolean isEqualTo = true;
    public boolean lesserThan = false;

    public TriggerPoints() {
        super(ItemCriteria.getStackFromMeta(ItemCriteria.ItemMeta.onReceivedPoints), "points", 0xFFB2B200);
    }
    
    @Override
    public EventBus getEventBus() {
        return null;
    }

    private boolean isValidValue(double total) {
        if (greaterThan && total > amount) return true;
        if (isEqualTo && total == amount) return true;
        if (lesserThan && total < amount) return true;

        //FALSE BABY!!!
        return false;
    }

    @Override
    public boolean onFired(UUID uuid, Object... data) {
        double total = ProgressionAPI.player.getDouble(uuid, variable);
        if (isValidValue(total)) {
            markTrue();
            if (consume) {
                PlayerTracker.getServerPlayer(uuid).addDouble(variable, -amount);
            }
        }

        return true;
    }
}
