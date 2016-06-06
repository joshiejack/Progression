package joshie.progression.criteria.filters.entity;

import com.google.common.collect.Lists;
import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.criteria.IFilter;
import joshie.progression.api.criteria.IFilterProvider;
import joshie.progression.api.criteria.IFilterType;
import joshie.progression.helpers.EntityHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public abstract class FilterBaseEntity implements IFilter<List<EntityLivingBase>, EntityLivingBase> {
    private IFilterProvider provider;

    @Override
    public IFilterProvider getProvider() {
        return provider;
    }

    @Override
    public void setProvider(IFilterProvider provider) {
        this.provider = provider;
    }

    @Override
    public List<EntityLivingBase> getRandom(EntityPlayer player) {
        return Lists.newArrayList(EntityHelper.getRandomEntity(player.worldObj, this.getProvider()));
    }

    @Override
    public void apply(EntityLivingBase entity) {}

    @Override
    public boolean matches(Object object) {
        if (!(object instanceof EntityLivingBase)) return false;
        return matches((EntityLivingBase) object);
    }

    @Override
    public IFilterType getType() {
        return ProgressionAPI.filters.getEntityFilter();
    }

    protected abstract boolean matches(EntityLivingBase entity);
}