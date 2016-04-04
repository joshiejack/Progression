package joshie.progression.criteria.triggers;

import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.criteria.ITrigger;
import joshie.progression.api.criteria.ProgressionRule;
import joshie.progression.api.special.DisplayMode;
import joshie.progression.api.special.ICustomWidth;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

@ProgressionRule(name="tick", color=0xFFA300D9, meta="onSecond")
public class TriggerTick extends TriggerBaseAlwaysTrue implements ICustomWidth {
    @Override
    public ITrigger copy() {
        return new TriggerTick();
    }

    @Override
    public int getWidth(DisplayMode mode) {
        return 75;
    }

    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent event) {
        if (event.phase != Phase.END) return;
        if (event.player.worldObj.getTotalWorldTime() % 20 == 0) {
            ProgressionAPI.registry.fireTrigger(event.player, getProvider().getUnlocalisedName());
        }
    }
}
