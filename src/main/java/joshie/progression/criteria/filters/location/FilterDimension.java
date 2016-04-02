package joshie.progression.criteria.filters.location;

import joshie.progression.Progression;
import joshie.progression.helpers.DimensionHelper;
import joshie.progression.lib.WorldLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

public class FilterDimension extends FilterLocationBase {
    public int dimensionID;

    public FilterDimension() {
        super("dimensionspawn", 0xFFBBBBBB);
    }

    @Override
    public WorldLocation getRandom(EntityPlayer player) {
        WorldServer world = DimensionManager.getWorld(dimensionID);
        if (world == null) return null;
        else return new WorldLocation(dimensionID, world.getSpawnCoordinate());
    }

    @Override
    public String getDescription() {
        return Progression.format("filter.location.dimensionspawn.description", DimensionHelper.getDimensionNameFromID(dimensionID));
    }

    @Override
    public boolean matches(WorldLocation location) {
        return location.dimension == dimensionID;
    }
}
