package joshie.progression.criteria.filters.location;

import java.util.ArrayList;
import java.util.List;

import joshie.progression.lib.WorldLocation;
import net.minecraft.entity.player.EntityPlayer;

public class FilterPlayerLocationAbove extends FilterLocationBase {
    public int maxYAbove = 32;

    public FilterPlayerLocationAbove() {
        super("playerAbove", 0xFFBBBBBB);
    }

    @Override
    public List<WorldLocation> getMatches(Object object) {
        if (object instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) object;
            ArrayList<WorldLocation> locations = new ArrayList();
            int randY = maxYAbove >= 1 ? player.worldObj.rand.nextInt(maxYAbove) : 1;
            WorldLocation location = new WorldLocation(player.dimension, player.posX, player.posY + randY, player.posZ);
            locations.add(location);
            return locations;
        }

        return new ArrayList();
    }
}
