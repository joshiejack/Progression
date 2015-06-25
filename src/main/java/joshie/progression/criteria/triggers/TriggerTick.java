package joshie.progression.criteria.triggers;

import joshie.progression.api.EventBusType;
import joshie.progression.api.ProgressionAPI;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class TriggerTick extends TriggerBaseBoolean {
    public TriggerTick() {
        super("tick", 0xFFA300D9);
    }

    @Override
    public EventBusType getEventBus() {
        return EventBusType.FML;
    }

    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent event) {
        if (event.phase != Phase.END) return;
        if (event.player.worldObj.getTotalWorldTime() % 20 == 0) {
            ProgressionAPI.registry.fireTrigger(event.player, getUnlocalisedName());
        }
    }

    @Override
    protected boolean isTrue(Object... data) {
        return true;
    }
}
