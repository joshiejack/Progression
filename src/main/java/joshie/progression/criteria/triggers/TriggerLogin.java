package joshie.progression.criteria.triggers;

import joshie.progression.api.ProgressionAPI;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

public class TriggerLogin extends TriggerBaseCounterVaries {
    public TriggerLogin() {
        super(new ItemStack(Items.fishing_rod), "login", 0xFF8000FF);
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
