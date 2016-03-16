package joshie.progression.criteria.triggers;

import joshie.progression.api.IHasEventBus;
import joshie.progression.api.ProgressionAPI;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.AchievementEvent;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TriggerAchievement extends TriggerBaseBoolean implements IHasEventBus {
    public String id = "openInventory";

    public TriggerAchievement() {
        super(new ItemStack(Items.book), "achievement", 0xFF00D9D9);
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
        return ((Achievement) data[0]).statId.equals(id);
    }
}
