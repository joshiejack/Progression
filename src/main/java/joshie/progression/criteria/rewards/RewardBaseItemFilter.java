package joshie.progression.criteria.rewards;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;

import joshie.progression.api.IItemFilter;
import joshie.progression.gui.fields.ItemFilterField;
import joshie.progression.gui.fields.ItemFilterFieldPreview;
import joshie.progression.helpers.ItemHelper;
import joshie.progression.helpers.JSONHelper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public abstract class RewardBaseItemFilter extends RewardBase {
    public static final ItemStack BROKEN = new ItemStack(Items.baked_potato);
    public List<IItemFilter> filters = new ArrayList();
    protected ItemStack preview;
    protected int ticker;

    public RewardBaseItemFilter(String name, int color) {
        super(name, color);
        list.add(new ItemFilterField("filters", this));
        list.add(new ItemFilterFieldPreview("filters", this, false, 25, 30, 26, 70, 25, 75, 2.8F));
    }

    @Override
    public void readFromJSON(JsonObject data) {
        filters = JSONHelper.getItemFilters(data, "filters");
    }

    @Override
    public void writeToJSON(JsonObject data) {
        JSONHelper.setItemFilters(data, "filters", filters);
    }
    
    @Override
    public ItemStack getIcon() {
        if (ticker == 0 || ticker >= 200) {
            preview = ItemHelper.getRandomItem(filters, false);
            ticker = 1;
        }
        
        ticker++;
        
        return preview == null ? BROKEN: preview;
    }
}
