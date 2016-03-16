package joshie.progression.criteria.triggers;

import joshie.progression.api.EventBusType;
import joshie.progression.api.ProgressionAPI;
import net.minecraft.stats.Achievement;
import net.minecraftforge.event.entity.player.AchievementEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TriggerAchievement extends TriggerBaseBoolean {
    public String id = "openInventory";

    public TriggerAchievement() {
        super("achievement", 0xFF00D9D9, EventBusType.FORGE);
    }

    @SubscribeEvent
    public void onAchievementGet(AchievementEvent event) {
        ProgressionAPI.registry.fireTrigger(event.entityPlayer, getUnlocalisedName(), event.achievement);
    }

    @Override
    protected boolean isTrue(Object... data) {
        return ((Achievement) data[0]).statId.equals(id);
    }
}
