package joshie.progression.criteria.filters.entity;

import java.util.List;

import joshie.progression.criteria.filters.FilterBase;
import joshie.progression.helpers.ItemHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public abstract class FilterBaseEntity extends FilterBase {
    public FilterBaseEntity(String string, int color) {
        super(string, color);
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
        if (!(object instanceof EntityLivingBase)) return false;
        return matches((EntityLivingBase) object);
    }

    @Override
    public FilterType getType() {
        return FilterType.ENTITY;
    }

    protected abstract boolean matches(EntityLivingBase entity);
}
