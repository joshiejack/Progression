package joshie.progression.criteria.triggers;

import joshie.progression.Progression;
import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.criteria.IProgressionTrigger;
import joshie.progression.items.ItemCriteria;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

public class TriggerLogin extends TriggerBaseCounter {
    public TriggerLogin() {
        super(ItemCriteria.getStackFromMeta(ItemCriteria.ItemMeta.onLogin), "login", 0xFF8000FF);
    }

    @Override
    public IProgressionTrigger copy() {
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
    public int getWidth() {
        return 65;
    }

    @Override
    public String getDescription() {
        return Progression.format("trigger.login.description", amount) + "\n\n" + Progression.format("completed", getPercentage());
    }
}
