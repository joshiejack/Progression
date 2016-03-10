package joshie.progression.criteria.filters;

import com.google.gson.JsonObject;

import joshie.progression.gui.newversion.overlays.FeatureItemSelector.Type;
import joshie.progression.gui.fields.ItemField;
import joshie.progression.helpers.JSONHelper;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class FilterItem extends FilterBase {
    public Item item = Items.beef;

    public FilterItem() {
        super("itemOnly");
        list.add(new ItemField("item", this, 76, 44, 1.4F, 77, 100, 43, 68, Type.TRIGGER));
    }

    @Override
    public void readFromJSON(JsonObject data) {
    	item = JSONHelper.getItem(data, "item", item);
    }

    @Override
    public void writeToJSON(JsonObject data) {
        JSONHelper.setItem(data, "item", item);
    }

    @Override
    public boolean matches(ItemStack check) {
    	return item == check.getItem();
    }
}
