package joshie.progression.criteria.filters.entity;

import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.criteria.IProgressionFilterSelector;
import joshie.progression.criteria.filters.FilterBase;
import joshie.progression.helpers.ItemHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

import java.util.List;

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
    public IProgressionFilterSelector getType() {
        return ProgressionAPI.filters.getEntityFilter();
    }

    protected abstract boolean matches(EntityLivingBase entity);
}
