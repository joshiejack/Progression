package joshie.progression.criteria.filters.location;

import joshie.progression.api.criteria.ProgressionRule;
import joshie.progression.lib.WorldLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

@ProgressionRule(name="randomy", color=0xFFBBBBBB)
public class FilterRandomY extends FilterRandomCoordinate {
    public FilterRandomY() {
        super("randomy");
    }

    @Override
    public BlockPos getLocation(EntityPlayer player, int random) {
        return new BlockPos(player.posX, coordinate + random, player.posZ);
    }

    @Override
    public int getCoordinate(WorldLocation location) {
        return location.pos.getY();
    }
}
