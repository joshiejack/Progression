package joshie.progression.criteria.filters.location;

import joshie.progression.lib.WorldLocation;
import net.minecraft.entity.player.EntityPlayer;

public class FilterPlayerLocationAround extends FilterLocationBase {
    public int randomX = 8;
    public int randomY = 2;
    public int randomZ = 8;

    public FilterPlayerLocationAround() {
        super("playerPos", 0xFFBBBBBB);
    }

    @Override
    public WorldLocation getRandom(EntityPlayer player) {
        int randX = randomX >= 1 ? player.worldObj.rand.nextInt(randomX) - (randomX / 2) : 0;
        int randY = randomY >= 1 ? player.worldObj.rand.nextInt(randomY) - (randomY / 2) : 0;
        int randZ = randomZ >= 1 ? player.worldObj.rand.nextInt(randomZ) - (randomZ / 2) : 0;
        return new WorldLocation(player.dimension, player.posX + randX, player.posY + randY, player.posZ + randZ);
    }

    @Override
    public boolean matches(WorldLocation location) {
        return true;
    }
}
