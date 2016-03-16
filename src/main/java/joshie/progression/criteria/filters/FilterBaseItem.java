package joshie.progression.criteria.filters;

import net.minecraft.item.ItemStack;

public abstract class FilterBaseItem extends FilterBase {
    public FilterBaseItem(String name, int color) {
        super(name, color);
    }

    @Override
    public boolean matches(Object object) {
        return object instanceof ItemStack ? matches((ItemStack)object) : false;
    }
    
    public abstract boolean matches(ItemStack stack);
}
