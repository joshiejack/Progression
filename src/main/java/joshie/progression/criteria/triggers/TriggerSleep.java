package joshie.progression.criteria.triggers;

import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.criteria.ITrigger;
import joshie.progression.api.criteria.ProgressionRule;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@ProgressionRule(name="sleep", color=0xFF831113, icon="minecraft:bed")
public class TriggerSleep extends TriggerBaseCounter  {
    @Override
    public ITrigger copy() {
        return copyCounter(new TriggerSleep());
    }

    @Override
    protected boolean canIncrease() {
        return true;
    }

    @SubscribeEvent
    public void onPlayerWakeup(PlayerWakeUpEvent event) {
        ProgressionAPI.registry.fireTrigger(event.entityPlayer, getProvider().getUnlocalisedName());
    }
}