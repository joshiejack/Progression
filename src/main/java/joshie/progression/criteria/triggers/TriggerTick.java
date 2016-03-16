package joshie.progression.criteria.triggers;

import joshie.progression.api.ProgressionAPI;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class TriggerTick extends TriggerBaseBoolean {
    public TriggerTick() {
        super("tick", 0xFFA300D9);
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
