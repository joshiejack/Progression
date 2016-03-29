package joshie.progression.criteria.filters.item;

import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.criteria.IProgressionFilterSelector;
import joshie.progression.criteria.filters.FilterBase;
import joshie.progression.helpers.ItemHelper;
import net.minecraft.item.ItemStack;

import java.util.List;

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
    public IProgressionFilterSelector getType() {
        return ProgressionAPI.filters.getItemStackFilter();
    }
    
    public abstract boolean matches(ItemStack stack);
}
