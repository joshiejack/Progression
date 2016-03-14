package joshie.progression.criteria.filters;

import com.google.gson.JsonObject;

import joshie.progression.gui.fields.TextField;
import joshie.progression.helpers.JSONHelper;
import joshie.progression.helpers.StackHelper;
import joshie.progression.lib.SafeStack;
import joshie.progression.lib.SafeStack.SafeStackMod;
import net.minecraft.item.ItemStack;

public class FilterMod extends FilterBase {
    public String modid = "minecraft";

    public FilterMod() {
        super("modid", 0xFFFF8000);
        list.add(new TextField("modid", this));
    }

    @Override
    public void readFromJSON(JsonObject data) {
    	modid = JSONHelper.getString(data, "modid", modid);
    }

    @Override
    public void writeToJSON(JsonObject data) {
        JSONHelper.setString(data, "modid", modid, "minecraft");
    }

    @Override
    public boolean matches(ItemStack check) {
    	return StackHelper.getModFromItem(check.getItem()).equals(modid);
    }
}
