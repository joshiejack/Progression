package joshie.progression.criteria.triggers;

import java.util.HashSet;
import java.util.Set;

import com.google.gson.JsonObject;

import joshie.progression.api.IItemFilter;
import joshie.progression.api.ProgressionAPI;
import joshie.progression.helpers.JSONHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerUseItemEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

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
        filters = JSONHelper.getItemFilters(data, "filters");
    }

    @Override
    public void writeToJSON(JsonObject data) {
        super.writeToJSON(data);
        JSONHelper.setItemFilters(data, "filters", filters);
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
