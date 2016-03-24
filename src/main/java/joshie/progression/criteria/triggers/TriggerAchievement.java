package joshie.progression.criteria.triggers;

import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.special.IHasEventBus;
import joshie.progression.items.ItemCriteria;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.AchievementEvent;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TriggerAchievement extends TriggerBaseBoolean implements IHasEventBus {
    public String id = "openInventory";

    public TriggerAchievement() {
        super(ItemCriteria.getStackFromMeta(ItemCriteria.ItemMeta.onReceivedAchiement), "achievement", 0xFF00D9D9);
    }
    
    @Override
    public EventBus getEventBus() {
        return MinecraftForge.EVENT_BUS;
    }

    @SubscribeEvent
    public void onAchievementGet(AchievementEvent event) {
        ProgressionAPI.registry.fireTrigger(event.entityPlayer, getUnlocalisedName(), event.achievement);
    }

    @Override
    protected boolean isTrue(Object... data) {
        return ((Achievement) data[0]).statId.equals("achievement." + id);
    }
}
