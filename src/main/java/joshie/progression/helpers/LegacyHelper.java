package joshie.progression.helpers;

import java.util.Set;

import com.google.gson.JsonObject;

import joshie.progression.api.IItemFilter;
import joshie.progression.criteria.filters.FilterItem;
import net.minecraft.item.ItemStack;

public class LegacyHelper {
    public static void readLegacyItems(JsonObject data, Set<IItemFilter> filters) {
        //Legacy Data
        ItemStack stack = JSONHelper.getItemStack(data, "item", null);
        boolean matchDamage = JSONHelper.getBoolean(data, "matchDamage", true);
        boolean matchNBT = JSONHelper.getBoolean(data, "matchNBT", false);

        if (stack != null) {
            FilterItem item = new FilterItem();
            item.stack = stack;
            item.matchDamage = matchDamage;
            item.matchNBT = matchNBT;
            filters.add(item);
        }
    }
}
