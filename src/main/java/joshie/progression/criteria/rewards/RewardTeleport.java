package joshie.progression.criteria.rewards;

import joshie.progression.Progression;
import joshie.progression.api.criteria.IField;
import joshie.progression.api.criteria.IFilterProvider;
import joshie.progression.api.criteria.IFilterType;
import joshie.progression.api.criteria.ProgressionRule;
import joshie.progression.api.special.DisplayMode;
import joshie.progression.api.special.ICustomDescription;
import joshie.progression.api.special.IHasFilters;
import joshie.progression.api.special.ISpecialFieldProvider;
import joshie.progression.gui.fields.ItemFilterField;
import joshie.progression.gui.filters.FilterTypeLocation;
import joshie.progression.lib.WorldLocation;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

import java.util.ArrayList;
import java.util.List;

@ProgressionRule(name="teleport", color=0xFFDDDDDD, icon="minecraft:ender_pearl")
public class RewardTeleport extends RewardBase implements ICustomDescription, IHasFilters, ISpecialFieldProvider {
    public List<IFilterProvider> locations = new ArrayList();
    protected transient IField field;

    public RewardTeleport() {
        field = new ItemFilterField("locations", this);
    }

    @Override
    public String getDescription() {
        return Progression.translate(getProvider().getUnlocalisedName() + ".description") + " \n" + field.getField();
    }

    @Override
    public List<IFilterProvider> getAllFilters() {
        return locations;
    }

    @Override
    public IFilterType getFilterForField(String fieldName) {
        return FilterTypeLocation.INSTANCE;
    }

    @Override
    public void addSpecialFields(List<IField> fields, DisplayMode mode) {
        fields.add(new ItemFilterField("locations", this));
    }

    @Override
    public void reward(EntityPlayerMP player) {
        boolean notteleported = true;
        for (int i = 0; i < 10 && notteleported; i++) {
            WorldLocation location = WorldLocation.getRandomLocationFromFilters(locations, player);
            if (location != null) {
                World world = DimensionManager.getWorld(location.dimension);
                int dimension = location.dimension;
                if (world == null) continue; //NO!!!!
                if (player.dimension != dimension) { //From RFTools
                    int oldDimension = player.worldObj.provider.getDimension();
                    MinecraftServer server = player.worldObj.getMinecraftServer();
                    WorldServer worldServer = server.worldServerForDimension(dimension);
                    player.addExperienceLevel(0); //Fix levels
                    BlockPos pos = new BlockPos(location.pos);
                    worldServer.getMinecraftServer().getPlayerList().transferPlayerToDimension(player, dimension, new DimensionTeleportation(worldServer, new BlockPos(location.pos)));
                    player.setPositionAndUpdate(pos.getX() + 0.5D, pos.getY() + 1, pos.getZ() + 0.5D);
                    if (oldDimension == 1) {
                        player.setPositionAndUpdate(pos.getX() + 0.5D, pos.getY() + 1, pos.getZ() + 0.5D);
                        worldServer.spawnEntityInWorld(player);
                        worldServer.updateEntityWithOptionalForce(player, false);
                    }

                    notteleported = false;
                } else {
                    BlockPos pos = new BlockPos(location.pos);
                    if (world.isBlockLoaded(pos)) {
                        if (isValidLocation(world, pos)) {
                            notteleported = false;
                            player.setPositionAndUpdate(pos.getX() + 0.5D, pos.getY() + 1, pos.getZ() + 0.5D);
                        }
                    }
                }
            }
        }
    }

    //Helper Methods
    private boolean isValidLocation(World world, BlockPos pos) {
        IBlockState floorState = world.getBlockState(pos);
        IBlockState feetState = world.getBlockState(pos.up());
        IBlockState headState = world.getBlockState(pos.up(2));
        Material floor = floorState.getBlock().getMaterial(floorState);
        Material feet = feetState.getBlock().getMaterial(feetState);
        Material head = headState.getBlock().getMaterial(headState);
        if (feet.blocksMovement()) return false;
        if (head.blocksMovement()) return false;
        if (floor.isLiquid() || feet.isLiquid() || head.isLiquid()) return false;
        return floor.blocksMovement();
    }

    public static class DimensionTeleportation extends Teleporter {
        private final WorldServer world;
        private final BlockPos pos;

        public DimensionTeleportation(WorldServer world, BlockPos pos) {
            super(world);
            this.world = world;
            this.pos = pos;
        }

        @Override
        public void placeInPortal(Entity entity, float rotationYaw) {
            world.getBlockState(pos);
            entity.setPositionAndUpdate(pos.getX() + 0.5D, pos.getY() + 1, pos.getZ() + 0.5D);
            entity.motionX = 0.0f;
            entity.motionY = 0.0f;
            entity.motionZ = 0.0f;
        }
    }
}
