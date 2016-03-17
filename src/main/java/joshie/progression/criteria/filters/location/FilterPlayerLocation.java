package joshie.progression.criteria.filters.location;

import java.util.ArrayList;
import java.util.List;

import joshie.progression.lib.WorldLocation;
import net.minecraft.entity.player.EntityPlayer;

public class FilterPlayerLocation extends FilterLocationBase {
    public int randomX = 32;
    public int randomY = 32;
    public int randomZ = 32;

    public FilterPlayerLocation() {
        super("playerPos", 0xFFBBBBBB);
    }

    @Override
    public List<WorldLocation> getMatches(Object object) {
        if (object instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) object;
            ArrayList<WorldLocation> locations = new ArrayList();
            int randX = randomX >= 1 ? player.worldObj.rand.nextInt(randomX) - (randomX / 2) : 0;
            int randY = randomX >= 1 ? player.worldObj.rand.nextInt(randomY) - (randomY / 2) : 0;
            int randZ = randomX >= 1 ? player.worldObj.rand.nextInt(randomZ) - (randomZ / 2) : 0;
            WorldLocation location = new WorldLocation(player.dimension, player.posX + randX, player.posY + randY, player.posZ + randZ);
            locations.add(location);
            return locations;
        }

        return new ArrayList();
    }

    @Override
    public boolean matches(WorldLocation location) {
        return true;
    }
}
