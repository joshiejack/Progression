package joshie.progression.criteria.filters;

import com.google.gson.JsonObject;

import joshie.progression.gui.fields.TextField;
import joshie.progression.helpers.JSONHelper;
import net.minecraft.item.ItemStack;

public class FilterMeta extends FilterBase {
    public int damage = 0;

    public FilterMeta() {
        super("metadata");
        list.add(new TextField("damage", this));
    }

    @Override
    public void readFromJSON(JsonObject data) {
    	damage = JSONHelper.getInteger(data, "damage", damage);
    }

    @Override
    public void writeToJSON(JsonObject data) {
        JSONHelper.setInteger(data, "damage", damage, 0);
    }

    @Override
    public boolean matches(ItemStack check) {
    	return check.getItemDamage() == damage;
    }
}
