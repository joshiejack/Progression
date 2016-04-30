package joshie.progression.criteria.filters.item;

import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.criteria.IField;
import joshie.progression.api.criteria.ProgressionRule;
import joshie.progression.api.special.DisplayMode;
import joshie.progression.api.special.IItemGetterCallback;
import joshie.progression.api.special.ISetterCallback;
import joshie.progression.api.special.ISpecialFieldProvider;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;

@ProgressionRule(name="itemOnly", color=0xFFCCCCCC)
public class FilterItem extends FilterBaseItem implements IItemGetterCallback, ISetterCallback, ISpecialFieldProvider {
    public Item item = Items.BEEF;

    @Override
    public void addSpecialFields(List<IField> fields, DisplayMode mode) {
        if (mode == DisplayMode.EDIT) fields.add(ProgressionAPI.fields.getItem(this, "item", 25, 25, 3F));
    }

    @Override
    public boolean matches(ItemStack check) {
        return item == check.getItem();
    }

    @Override
    public ItemStack getItem(String fieldName) {
        return new ItemStack(item);
    }

    @Override
    public boolean setField(String fieldName, Object stack) {
        item = ((ItemStack) stack).getItem();
        return true;
    }
}
