package joshie.progression.criteria.filters.location;

import joshie.progression.api.criteria.ProgressionRule;
import joshie.progression.api.special.IEnum;
import joshie.progression.lib.WorldLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

@ProgressionRule(name="randomx", color=0xFFBBBBBB)
public class FilterRandomX extends FilterRandomCoordinate implements IEnum {
    public FilterRandomX() {
        super("randomx");
    }

    @Override
    public BlockPos getLocation(EntityPlayer player, int random) {
        return new BlockPos(coordinate + random, player.posY, player.posZ);
    }

    @Override
    public int getCoordinate(WorldLocation location) {
        return location.pos.getX();
    }
}
