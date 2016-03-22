package joshie.progression.gui.filters;

import java.util.List;

import joshie.progression.api.criteria.IProgressionFilterSelector;
import joshie.progression.api.criteria.IProgressionFilter.FilterType;
import joshie.progression.helpers.ItemHelper;
import net.minecraft.item.ItemStack;

public class FilterSelectorItem extends FilterSelectorBase {
    public static final IProgressionFilterSelector INSTANCE = new FilterSelectorItem();
    
    @Override
    public List<ItemStack> getAllItems() {
        return ItemHelper.getAllItems();
    }

    @Override
    public FilterType getType() {
        return FilterType.ITEM;
    }

    @Override
    public boolean isAcceptable(Object object) {
        return object instanceof ItemStack ? isAcceptedItem((ItemStack) object) : false;
    }

    public boolean isAcceptedItem(ItemStack object) {
        return true;
    }
}
