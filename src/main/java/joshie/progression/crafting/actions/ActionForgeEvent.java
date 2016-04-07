package joshie.progression.crafting.actions;

import joshie.progression.api.special.IHasEventBus;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventBus;

public class ActionForgeEvent implements IHasEventBus {
    @Override
    public EventBus getEventBus() {
        return MinecraftForge.EVENT_BUS;
    }
}
