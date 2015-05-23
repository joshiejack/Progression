package joshie.progression.criteria.triggers;

import joshie.progression.api.EventBusType;
import joshie.progression.api.ProgressionAPI;
import joshie.progression.gui.fields.TextField;
import joshie.progression.helpers.JSONHelper;
import net.minecraft.stats.Achievement;
import net.minecraftforge.event.entity.player.AchievementEvent;

import com.google.gson.JsonObject;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class TriggerAchievement extends TriggerBaseBoolean {
    public String id = "openInventory";

    public TriggerAchievement() {
        super("achievement", 0xFF00D9D9);
        list.add(new TextField("id", this));
    }

    @SubscribeEvent
    public void onAchievementGet(AchievementEvent event) {
        ProgressionAPI.registry.fireTrigger(event.entityPlayer, getUnlocalisedName(), event.achievement);
    }

    @Override
    public EventBusType getEventBus() {
        return EventBusType.FORGE;
    }

    @Override
    public void readFromJSON(JsonObject data) {
        id = JSONHelper.getString(data, "id", id);
    }

    @Override
    public void writeToJSON(JsonObject data) {
        JSONHelper.setString(data, "id", id, "research");
    }

    @Override
    protected boolean isTrue(Object... data) {
        return ((Achievement) data[0]).statId.equals(id);
    }
}
