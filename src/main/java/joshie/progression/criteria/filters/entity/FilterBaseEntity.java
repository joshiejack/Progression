package joshie.progression.criteria.filters.entity;

import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.criteria.IProgressionFilterSelector;
import joshie.progression.criteria.filters.FilterBase;
import joshie.progression.helpers.EntityHelper;
import joshie.progression.helpers.ItemHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public abstract class FilterBaseEntity extends FilterBase {
    public FilterBaseEntity(String string, int color) {
        super(string, color);
    }

    @Override
    public EntityLivingBase getRandom(EntityPlayer player) {
        return EntityHelper.getRandomEntity(this);
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
