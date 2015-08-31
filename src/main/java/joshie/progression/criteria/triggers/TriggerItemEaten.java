package joshie.progression.criteria.triggers;

import java.util.HashSet;
import java.util.Set;

import joshie.progression.api.IItemFilter;
import joshie.progression.api.ProgressionAPI;
import joshie.progression.criteria.filters.FilterItem;
import joshie.progression.helpers.JSONHelper;
import joshie.progression.helpers.LegacyHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerUseItemEvent;

import com.google.gson.JsonObject;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class TriggerItemEaten extends TriggerBaseCounter {
    public Set<IItemFilter> filters = new HashSet();

    public TriggerItemEaten() {
        super("onEaten", 0xFF00B285);
    }

    @SubscribeEvent
    public void onEvent(PlayerUseItemEvent.Finish event) {
        ProgressionAPI.registry.fireTrigger(event.entityPlayer, getUnlocalisedName(), event.item);
    }

    @Override
    public void readFromJSON(JsonObject data) {
        super.readFromJSON(data);
        filters = JSONHelper.getFilters(data, "filters");
        LegacyHelper.readLegacyItems(data, filters);
    }

    @Override
    public void writeToJSON(JsonObject data) {
        super.writeToJSON(data);
        JSONHelper.setFilters(data, "filters", filters);
    }

    @Override
    protected boolean canIncrease(Object... data) {
        ItemStack item = (ItemStack) data[0];
        for (IItemFilter filter: filters) {
            if (filter.matches((ItemStack)data[0])) return true;
        }
        
        return false;
    }
}
