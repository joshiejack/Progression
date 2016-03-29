package joshie.progression.criteria.filters.location;

import joshie.progression.lib.WorldLocation;
import net.minecraft.entity.player.EntityPlayer;

public class FilterRandomLocationDimension extends FilterLocationBase {
    public int dimension;

    public FilterRandomLocationDimension() {
        super("dimension", 0xFFBBBBBB);
    }

    @Override
    public WorldLocation getRandom(EntityPlayer player) {
        if (player.dimension == dimension) {
            return new WorldLocation(dimension, player.posX + player.worldObj.rand.nextInt(32) - 16, player.posY + player.worldObj.rand.nextInt(32) - 16, player.posZ + player.worldObj.rand.nextInt(32) - 16);
        }

        return null;
    }

    @Override
    public boolean matches(WorldLocation location) {
        return location.dimension == dimension;
    }
}
