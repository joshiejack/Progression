package joshie.progression.criteria.triggers;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;

import joshie.progression.api.IItemFilter;
import joshie.progression.api.ProgressionAPI;
import joshie.progression.gui.fields.ItemFilterField;
import joshie.progression.gui.fields.ItemFilterFieldPreview;
import joshie.progression.helpers.JSONHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerUseItemEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TriggerItemEaten extends TriggerBaseCounter {
    public List<IItemFilter> filters = new ArrayList();

    public TriggerItemEaten() {
        super("onEaten", 0xFF00B285);
        list.add(new ItemFilterField("filters", this));
        list.add(new ItemFilterFieldPreview("filters", this, false, 25, 30, 26, 70, 25, 75, 2.8F));
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
