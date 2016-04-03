package joshie.progression.gui.filters;

import joshie.progression.api.criteria.IFilterType;
import joshie.progression.helpers.ItemHelper;
import net.minecraft.item.ItemStack;

import java.util.List;

public class FilterTypeItem extends FilterTypeBase {
    public static final IFilterType INSTANCE = new FilterTypeItem();

    @Override
    public String getName() {
        return "item";
    }

    @Override
    public List<ItemStack> getAllItems() {
        return ItemHelper.getAllItems();
    }

    @Override
    public boolean isAcceptable(Object object) {
        return object instanceof ItemStack ? isAcceptedItem((ItemStack) object) : false;
    }

    public boolean isAcceptedItem(ItemStack object) {
        return true;
    }
}
