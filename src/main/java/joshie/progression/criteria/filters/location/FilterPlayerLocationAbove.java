package joshie.progression.criteria.filters.location;

import joshie.progression.lib.WorldLocation;
import net.minecraft.entity.player.EntityPlayer;

public class FilterPlayerLocationAbove extends FilterLocationBase {
    public int maxYAbove = 32;

    public FilterPlayerLocationAbove() {
        super("playerAbove", 0xFFBBBBBB);
    }

    @Override
    public WorldLocation getRandom(EntityPlayer player) {
        int randY = maxYAbove >= 1 ? player.worldObj.rand.nextInt(maxYAbove) : 1;
        return new WorldLocation(player.dimension, player.posX, player.posY + randY, player.posZ);
    }
}
