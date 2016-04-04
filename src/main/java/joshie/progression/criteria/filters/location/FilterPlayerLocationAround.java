package joshie.progression.criteria.filters.location;

import joshie.progression.api.criteria.ProgressionRule;
import joshie.progression.lib.WorldLocation;
import net.minecraft.entity.player.EntityPlayer;

@ProgressionRule(name="playerPos", color=0xFFBBBBBB)
public class FilterPlayerLocationAround extends FilterLocationBase {
    public int randomX = 8;
    public int randomY = 2;
    public int randomZ = 8;

    @Override
    public WorldLocation getRandom(EntityPlayer player) {
        int randX = randomX >= 1 ? player.worldObj.rand.nextInt(randomX * 2) - randomX : 0;
        int randY = randomY >= 1 ? player.worldObj.rand.nextInt(randomY * 2) - randomY : 0;
        int randZ = randomZ >= 1 ? player.worldObj.rand.nextInt(randomZ * 2) - randomZ : 0;
        return new WorldLocation(player.dimension, player.posX + randX, player.posY + randY, player.posZ + randZ);
    }

    @Override
    public boolean matches(WorldLocation location) {
        return true; //Always true as it's a location around the player
    }
}
