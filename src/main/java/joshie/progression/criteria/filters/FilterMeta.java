package joshie.progression.criteria.filters;

import net.minecraft.item.ItemStack;

public class FilterMeta extends FilterBase {
    public int damage = 0;

    public FilterMeta() {
        super("metadata", 0xFFFF73FF);
    }

    @Override
    public boolean matches(ItemStack check) {
        return check.getItemDamage() == damage || damage == Short.MAX_VALUE;
    }
}
