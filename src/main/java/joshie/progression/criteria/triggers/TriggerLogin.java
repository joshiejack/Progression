package joshie.progression.criteria.triggers;

import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.criteria.ITrigger;
import joshie.progression.api.criteria.ProgressionRule;
import joshie.progression.api.special.DisplayMode;
import joshie.progression.api.special.ICustomWidth;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

@ProgressionRule(name="login", color=0xFF8000FF, meta="onLogin")
public class TriggerLogin extends TriggerBaseCounter implements ICustomWidth {
    @Override
    public ITrigger copy() {
        return copyCounter(new TriggerLogin());
    }

    @Override
    public int getWidth(DisplayMode mode) {
        return mode == DisplayMode.EDIT ? 75 : 65;
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onEvent(PlayerLoggedInEvent event) {
        ProgressionAPI.registry.fireTrigger(event.player, getProvider().getUnlocalisedName());
    }

    @Override
    protected boolean canIncrease() {
        return true;
    }
}
