package joshie.progression.criteria.triggers;

import joshie.progression.Progression;
import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.criteria.ITrigger;
import joshie.progression.api.criteria.ProgressionRule;
import joshie.progression.api.special.ICustomDescription;
import joshie.progression.player.PlayerTracker;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.common.eventhandler.EventBus;

import java.util.UUID;

@ProgressionRule(name="points", color=0xFFB2B200, meta="onReceivedPoints")
public class TriggerPoints extends TriggerBaseBoolean implements ICustomDescription {
    public String variable = "gold";
    public double amount = 1D;
    public boolean consume = true;

    @Override
    public ITrigger copy() {
        TriggerPoints trigger = new TriggerPoints();
        trigger.variable = variable;
        trigger.amount = amount;
        trigger.consume = consume;
        return copyBoolean(trigger);
    }

    @Override
    public String getDescription() {
        String extra = consume ? "\n" + EnumChatFormatting.ITALIC + Progression.format("trigger.points.extra", variable) : "";
        String value = (amount == (long) amount) ? String.format("%d", (long) amount): String.format("%s", amount);
        return Progression.format("trigger.points.description", value, variable, extra) + "\n\n" + Progression.format("completed", getPercentage());
    }
    
    @Override
    public EventBus getEventBus() {
        return null;
    }

    @Override
    public boolean onFired(UUID uuid, Object... data) {
        double total = ProgressionAPI.player.getDouble(uuid, variable);
        if (total >= amount) {
            markTrue();
            if (consume) {
                PlayerTracker.getServerPlayer(uuid).addDouble(variable, -amount);
            }
        }

        return true;
    }
}
