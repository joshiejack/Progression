package joshie.progression.criteria.filters.location;

import joshie.progression.lib.WorldLocation;
import net.minecraft.entity.player.EntityPlayer;

public class FilterRandomAround extends FilterLocationBase {
    public int xCoordinate = 0;
    public int yCoordinate = 64;
    public int zCoordinate = 0;
    public int randomX = 32;
    public int randomY = 32;
    public int randomZ = 32;

    public FilterRandomAround() {
        super("randomaround", 0xFFBBBBBB);
    }

    @Override
    public WorldLocation getRandom(EntityPlayer player) {
        int randX = randomX >= 1 ? player.worldObj.rand.nextInt(randomX) - (randomX / 2) : 0;
        int randY = randomY >= 1 ? player.worldObj.rand.nextInt(randomY) - (randomY / 2) : 0;
        int randZ = randomZ >= 1 ? player.worldObj.rand.nextInt(randomZ) - (randomZ / 2) : 0;
        return new WorldLocation(player.dimension, xCoordinate + randX, yCoordinate + randY, zCoordinate + randZ);
    }

    @Override
    public boolean matches(WorldLocation location) {
        return true;
    }
}
