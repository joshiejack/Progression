package joshie.progression.criteria.filters.entity;

import com.google.common.collect.Lists;
import joshie.progression.api.criteria.ProgressionRule;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

@ProgressionRule(name="witherskeleton", color=0xFFB25900)
public class FilterSkeletonType extends FilterBaseEntity {
    public boolean wither = true;

    @Override
    public List<EntityLivingBase> getRandom(EntityPlayer player) {
        return Lists.newArrayList(new EntitySkeleton(player.worldObj));
    }

    @Override
    public void apply(EntityLivingBase entity) {
        if (entity instanceof EntitySkeleton) {
            EntitySkeleton skeleton = ((EntitySkeleton) entity);
            if (wither) skeleton.setSkeletonType(1);
            else skeleton.setSkeletonType(0);
        }
    }

    @Override
    protected boolean matches(EntityLivingBase entity) {
        if (!(entity instanceof EntitySkeleton)) return false;
        if (wither) return ((EntitySkeleton) entity).getSkeletonType() == 1;
        else return ((EntitySkeleton) entity).getSkeletonType() == 0;
    }
}