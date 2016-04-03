package joshie.progression.criteria.triggers;

import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.criteria.ITrigger;
import joshie.progression.api.special.DisplayMode;
import joshie.progression.items.ItemCriteria;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

public class TriggerLogin extends TriggerBaseCounter {
    public TriggerLogin() {
        super(ItemCriteria.getStackFromMeta(ItemCriteria.ItemMeta.onLogin), "login", 0xFF8000FF);
    }

    @Override
    public ITrigger copy() {
        TriggerLogin trigger = new TriggerLogin();
        return copyBase(copyCounter(trigger));
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onEvent(PlayerLoggedInEvent event) {
        ProgressionAPI.registry.fireTrigger(event.player, getUnlocalisedName());
    }

    @Override
    protected boolean canIncrease() {
        return true;
    }

    @Override
    public int getWidth(DisplayMode mode) {
        return mode == DisplayMode.EDIT ? super.getWidth(mode) : 65;
    }
}
