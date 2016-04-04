package joshie.progression.criteria.filters.entity;

import joshie.progression.api.criteria.ProgressionRule;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySkeleton;

@ProgressionRule(name="witherskeleton", color=0xFFB25900)
public class FilterSkeletonType extends FilterBaseEntity {
    public boolean wither = true;

    @Override
    protected boolean matches(EntityLivingBase entity) {
        if (!(entity instanceof EntitySkeleton)) return false;
        if (wither) return ((EntitySkeleton) entity).getSkeletonType() == 1;
        else return ((EntitySkeleton) entity).getSkeletonType() == 0;
    }
}
