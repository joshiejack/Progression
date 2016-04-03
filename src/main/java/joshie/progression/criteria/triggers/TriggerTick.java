package joshie.progression.criteria.triggers;

import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.criteria.ITrigger;
import joshie.progression.api.special.DisplayMode;
import joshie.progression.items.ItemCriteria;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class TriggerTick extends TriggerBaseAlwaysTrue {
    public TriggerTick() {
        super(ItemCriteria.getStackFromMeta(ItemCriteria.ItemMeta.onSecond), "tick", 0xFFA300D9);
    }

    @Override
    public ITrigger copy() {
        return copyBase(new TriggerTick());
    }

    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent event) {
        if (event.phase != Phase.END) return;
        if (event.player.worldObj.getTotalWorldTime() % 20 == 0) {
            ProgressionAPI.registry.fireTrigger(event.player, getUnlocalisedName());
        }
    }

    @Override
    public int getWidth(DisplayMode mode) {
        return 75;
    }

    @Override
    public String getDescription() {
        return getTriggerDescription();
    }
}
