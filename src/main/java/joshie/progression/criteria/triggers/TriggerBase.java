package joshie.progression.criteria.triggers;

import joshie.progression.api.criteria.ITrigger;
import joshie.progression.api.criteria.ITriggerProvider;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventBus;

public abstract class TriggerBase implements ITrigger {
    private ITriggerProvider provider;

    @Override
    public void setProvider(ITriggerProvider provider) {
        this.provider = provider;
    }

    @Override
    public ITriggerProvider getProvider() {
        return provider;
    }

    @Override
    public EventBus getEventBus() {
        return MinecraftForge.EVENT_BUS;
    }
}
