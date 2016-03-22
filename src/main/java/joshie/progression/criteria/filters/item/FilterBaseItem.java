package joshie.progression.criteria.filters.item;

import java.util.List;

import joshie.progression.criteria.filters.FilterBase;
import joshie.progression.helpers.ItemHelper;
import net.minecraft.item.ItemStack;

public abstract class FilterBaseItem extends FilterBase {
    public FilterBaseItem(String name, int color) {
        super(name, color);
    }
    
    @Override
    public List<ItemStack> getMatches(Object object) {
        return getMatches();
    }
    
    public List<ItemStack> getMatches() {
        return ItemHelper.getAllMatchingItems(this);
    }

    @Override
    public boolean matches(Object object) {
        return object instanceof ItemStack ? matches((ItemStack)object) : false;
    }
    
    @Override
    public FilterType getType() {
        return FilterType.ITEM;
    }
    
    public abstract boolean matches(ItemStack stack);
}
