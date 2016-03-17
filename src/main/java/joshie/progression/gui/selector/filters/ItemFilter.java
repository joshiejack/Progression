package joshie.progression.gui.selector.filters;

import java.util.List;

import joshie.progression.api.IFilter.FilterType;
import joshie.progression.gui.newversion.overlays.IFilterSelectorFilter;
import joshie.progression.helpers.ItemHelper;
import net.minecraft.item.ItemStack;

public class ItemFilter extends FilterBase {
    public static final IFilterSelectorFilter INSTANCE = new ItemFilter();
    
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
