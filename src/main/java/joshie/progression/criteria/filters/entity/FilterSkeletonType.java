package joshie.progression.criteria.filters.entity;

import joshie.progression.api.criteria.ProgressionRule;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.player.EntityPlayer;

@ProgressionRule(name="witherskeleton", color=0xFFB25900)
public class FilterSkeletonType extends FilterBaseEntity {
    public boolean wither = true;

    @Override
    public EntityLivingBase getRandom(EntityPlayer player) {
        EntitySkeleton skeleton = new EntitySkeleton(player.worldObj);
        if (wither) ((EntitySkeleton) skeleton).setSkeletonType(1);
        else ((EntitySkeleton) skeleton).setSkeletonType(0);
        return skeleton;
    }

    @Override
    protected boolean matches(EntityLivingBase entity) {
        if (!(entity instanceof EntitySkeleton)) return false;
        if (wither) return ((EntitySkeleton) entity).getSkeletonType() == 1;
        else return ((EntitySkeleton) entity).getSkeletonType() == 0;
    }
}
