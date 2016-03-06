package joshie.progression.criteria.triggers;

import joshie.progression.api.EventBusType;
import joshie.progression.api.ProgressionAPI;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

public class TriggerLogin extends TriggerBaseCounter {
    public TriggerLogin() {
        super("login", 0xFF8000FF);
    }

    @Override
    public EventBusType getEventBus() {
        return EventBusType.FML;
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onEvent(PlayerLoggedInEvent event) {
        ProgressionAPI.registry.fireTrigger(event.player, getUnlocalisedName());
    }

    @Override
    protected boolean canIncrease(Object... data) {
        return true;
    }
}
