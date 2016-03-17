package joshie.progression.criteria.filters.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySkeleton;

public class FilterSkeletonType extends FilterBaseEntity {
    public boolean wither = true;

    public FilterSkeletonType() {
        super("witherskeleton", 0xFFB25900);
    }

    @Override
    protected boolean matches(EntityLivingBase entity) {
        if (!(entity instanceof EntitySkeleton)) return false;
        if (wither) return ((EntitySkeleton) entity).getSkeletonType() == 1;
        else return ((EntitySkeleton) entity).getSkeletonType() == 0;
    }
}
