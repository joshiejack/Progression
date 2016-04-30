package joshie.progression.criteria.filters.location;

import joshie.progression.Progression;
import joshie.progression.api.criteria.ProgressionRule;
import joshie.progression.api.special.ICustomDescription;
import joshie.progression.helpers.DimensionHelper;
import joshie.progression.lib.WorldLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

@ProgressionRule(name="dimensionspawn", color=0xFFBBBBBB)
public class FilterDimension extends FilterLocationBase implements ICustomDescription {
    public int dimensionID;

    @Override
    public String getDescription() {
        return Progression.format("filter.location.dimensionspawn.description", DimensionHelper.getDimensionNameFromID(dimensionID));
    }

    @Override
    public WorldLocation getRandom(EntityPlayer player) {
        WorldServer world = DimensionManager.getWorld(dimensionID);
        if (world == null) return null;
        BlockPos pos = world.getSpawnCoordinate();
        if (pos == null) pos = world.getSpawnPoint();
        if (pos == null) return null;
        return new WorldLocation(dimensionID, pos);
    }

    @Override
    public boolean matches(WorldLocation location) {
        return location.dimension == dimensionID;
    }
}
