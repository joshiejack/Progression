package joshie.progression.criteria.filters.item;

import java.util.List;

import joshie.progression.api.IItemGetterCallback;
import joshie.progression.api.ISetterCallback;
import joshie.progression.api.fields.IField;
import joshie.progression.api.fields.ISpecialFieldProvider;
import joshie.progression.api.fields.ISpecialFieldProvider.DisplayMode;
import joshie.progression.gui.fields.ItemField;
import joshie.progression.gui.newversion.overlays.FeatureItemSelector.Type;
import joshie.progression.gui.selector.filters.BlockFilter;
import joshie.progression.gui.selector.filters.ItemFilter;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class FilterItem extends FilterBaseItem implements IItemGetterCallback, ISetterCallback, ISpecialFieldProvider {
    public Item item = Items.beef;

    public FilterItem() {
        super("itemOnly", 0xFFCCCCCC);
    }

    @Override
    public boolean shouldReflectionSkipField(String name) {
        return true;
    }

    @Override
    public void addSpecialFields(List<IField> fields, DisplayMode mode) {
        if (mode == DisplayMode.EDIT) fields.add(new ItemField("item", this, 25, 25, 3F, 26, 70, 25, 75, Type.TRIGGER, ItemFilter.INSTANCE));
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
