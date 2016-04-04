package joshie.progression.criteria.filters.location;

import joshie.progression.api.criteria.ProgressionRule;
import joshie.progression.api.special.IEnum;
import joshie.progression.lib.WorldLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;

@ProgressionRule(name="randomy", color=0xFFBBBBBB)
public class FilterRandomY extends FilterRandomCoordinate implements IEnum {
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
