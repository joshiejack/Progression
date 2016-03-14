package joshie.progression.criteria.filters;

import com.google.gson.JsonObject;

import joshie.progression.gui.newversion.overlays.FeatureItemSelector.Type;
import joshie.progression.gui.fields.BooleanField;
import joshie.progression.gui.fields.ItemField;
import joshie.progression.helpers.JSONHelper;
import joshie.progression.lib.SafeStack;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class FilterItemStack extends FilterBase {
    public ItemStack stack = new ItemStack(Items.arrow);
    public boolean matchDamage = true;
    public boolean matchNBT = false;

    public FilterItemStack() {
        super("itemStack", 0xFF663300);
        list.add(new BooleanField("matchDamage", this));
        list.add(new BooleanField("matchNBT", this));
        list.add(new ItemField("stack", this, 76, 44, 1.4F, 77, 100, 43, 68, Type.TRIGGER));
    }

    @Override
    public void readFromJSON(JsonObject data) {
        stack = JSONHelper.getItemStack(data, "item", stack);
        matchDamage = JSONHelper.getBoolean(data, "matchDamage", matchDamage);
        matchNBT = JSONHelper.getBoolean(data, "matchNBT", matchNBT);
    }

    @Override
    public void writeToJSON(JsonObject data) {
        JSONHelper.setItemStack(data, "item", stack);
        JSONHelper.setBoolean(data, "matchDamage", matchDamage, true);
        JSONHelper.setBoolean(data, "matchNBT", matchNBT, false);
    }

    @Override
    public boolean matches(ItemStack check) {
        if (stack.getItem() != check.getItem()) return false;
        if (matchDamage && (stack.getItemDamage() != check.getItemDamage())) return false;
        if (matchNBT && (!stack.getTagCompound().equals(check.getTagCompound()))) return false;
        return true;
    }
}
