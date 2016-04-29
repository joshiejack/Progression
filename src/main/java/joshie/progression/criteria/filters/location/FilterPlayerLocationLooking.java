package joshie.progression.criteria.filters.location;

import joshie.progression.Progression;
import joshie.progression.api.criteria.ProgressionRule;
import joshie.progression.api.special.ICustomDescription;
import joshie.progression.lib.WorldLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

@ProgressionRule(name="playerLook", color=0xFFBBBBBB)
public class FilterPlayerLocationLooking extends FilterLocationBase implements ICustomDescription {
    public boolean reachDistance = true;
    public double maximumDistance = 64;

    @Override
    public String getDescription() {
        if (reachDistance) return Progression.translate("filter.location.playerLook.reach");
        else return Progression.format("filter.location.playerLook.blocks", maximumDistance);
    }

    protected MovingObjectPosition getMovingObjectPositionFromPlayer(World worldIn, EntityPlayer player) {
        float f = player.rotationPitch;
        float f1 = player.rotationYaw;
        double d0 = player.posX;
        double d1 = player.posY + (double)player.getEyeHeight();
        double d2 = player.posZ;
        Vec3 vec3 = new Vec3(d0, d1, d2);
        float f2 = MathHelper.cos(-f1 * 0.017453292F - (float)Math.PI);
        float f3 = MathHelper.sin(-f1 * 0.017453292F - (float)Math.PI);
        float f4 = -MathHelper.cos(-f * 0.017453292F);
        float f5 = MathHelper.sin(-f * 0.017453292F);
        float f6 = f3 * f4;
        float f7 = f2 * f4;
        double d3 = 5.0D;
        if (reachDistance) {
            if (player instanceof EntityPlayerMP) {
                d3 = ((EntityPlayerMP) player).theItemInWorldManager.getBlockReachDistance();
            }
        } else d3 = maximumDistance;

        Vec3 vec31 = vec3.addVector((double)f6 * d3, (double)f5 * d3, (double)f7 * d3);
        return worldIn.rayTraceBlocks(vec3, vec31, false, true, false);
    }

    @Override
    public WorldLocation getRandom(EntityPlayer player) {
        MovingObjectPosition position = getMovingObjectPositionFromPlayer(player.worldObj, player);
        return position == null? null: new WorldLocation(player.dimension, position.getBlockPos());
    }
}
