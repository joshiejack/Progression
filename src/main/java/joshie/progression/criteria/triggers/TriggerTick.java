package joshie.progression.criteria.triggers;

import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.criteria.IProgressionTrigger;
import joshie.progression.items.ItemCriteria;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class TriggerTick extends TriggerBaseAlwaysTrue {
    public TriggerTick() {
        super(ItemCriteria.getStackFromMeta(ItemCriteria.ItemMeta.onSecond), "tick", 0xFFA300D9);
    }

    @Override
    public IProgressionTrigger copy() {
        return copyBase(new TriggerTick());
    }

    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent event) {
        if (event.phase != Phase.END) return;
        if (event.player.worldObj.getTotalWorldTime() % 20 == 0) {
            ProgressionAPI.registry.fireTrigger(event.player, getUnlocalisedName());
        }
    }
}
