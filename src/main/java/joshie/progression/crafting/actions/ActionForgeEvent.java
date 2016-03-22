package joshie.progression.crafting.actions;

import joshie.progression.api.special.IHasEventBus;
import joshie.progression.crafting.ActionType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventBus;

public class ActionForgeEvent extends ActionType implements IHasEventBus {
    public ActionForgeEvent(String name) {
        super(name);
    }

    @Override
    public EventBus getEventBus() {
        return MinecraftForge.EVENT_BUS;
    }
    
    public IHasEventBus getCustomBus() {
        return null;
    }
}
