package joshie.progression.criteria.filters.location;

import java.util.ArrayList;
import java.util.List;

import joshie.progression.lib.WorldLocation;
import net.minecraft.entity.player.EntityPlayer;

public class FilterRandomLocationDimension extends FilterLocationBase {
    public int dimension;

    public FilterRandomLocationDimension() {
        super("dimension", 0xFFBBBBBB);
    }

    @Override
    public List<WorldLocation> getMatches(Object object) {
        if (object instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) object;
            if (player.dimension == dimension) {
                ArrayList<WorldLocation> locations = new ArrayList();
                WorldLocation location = new WorldLocation(dimension, player.posX + player.worldObj.rand.nextInt(32) - 16, player.posY + player.worldObj.rand.nextInt(32) - 16, player.posZ + player.worldObj.rand.nextInt(32) - 16);
                locations.add(location);
                return locations;
            }
        }

        return new ArrayList();
    }

    @Override
    public boolean matches(WorldLocation location) {
        return location.dimension == dimension;
    }
}
