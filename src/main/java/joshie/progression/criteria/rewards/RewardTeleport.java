package joshie.progression.criteria.rewards;

import com.google.common.collect.Lists;
import joshie.progression.Progression;
import joshie.progression.api.criteria.*;
import joshie.progression.api.special.DisplayMode;
import joshie.progression.api.special.ICustomDescription;
import joshie.progression.api.special.IHasFilters;
import joshie.progression.api.special.ISpecialFieldProvider;
import joshie.progression.gui.fields.ItemFilterField;
import joshie.progression.gui.filters.FilterTypeEntity;
import joshie.progression.gui.filters.FilterTypeLocation;
import joshie.progression.helpers.EntityHelper;
import joshie.progression.lib.WorldLocation;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
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
    public List<IFilterProvider> targets = new ArrayList();
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
        List<IFilterProvider> list = Lists.newArrayList();
        list.addAll(locations);
        list.addAll(targets);
        return list;
    }

    @Override
    public IFilterType getFilterForField(String fieldName) {
        if (fieldName.equals("targets")) return FilterTypeEntity.INSTANCE;
        return FilterTypeLocation.INSTANCE;
    }

    @Override
    public void addSpecialFields(List<IField> fields, DisplayMode mode) {
        fields.add(new ItemFilterField("locations", this));
        fields.add(new ItemFilterField("targets", this));
    }

    @Override
    public void reward(EntityPlayerMP thePlayer) {
        boolean notteleported = true;
        for (int i = 0; i < 10 && notteleported; i++) {
            WorldLocation location = WorldLocation.getRandomLocationFromFilters(locations, thePlayer);
            if (location != null) {
                IFilter filter = EntityHelper.getFilter(targets, thePlayer);
                if (filter != null) {
                    List<EntityLivingBase> entities = (List<EntityLivingBase>) filter.getRandom(thePlayer);
                    for (EntityLivingBase entity : entities) {
                        World world = DimensionManager.getWorld(location.dimension);
                        int dimension = location.dimension;
                        if (world == null) continue; //NO!!!!
                        if (entity.dimension != dimension) { //From RFTools
                            MinecraftServer server = entity.worldObj.getMinecraftServer();
                            WorldServer worldServer = server.worldServerForDimension(dimension);
                            int oldDimension = entity.worldObj.provider.getDimension();
                            BlockPos pos = new BlockPos(location.pos);
                            if (entity instanceof EntityPlayer) {
                                ((EntityPlayerMP)entity).addExperienceLevel(0); //Fix levels
                                worldServer.getMinecraftServer().getPlayerList().transferPlayerToDimension(((EntityPlayerMP)entity), dimension, new DimensionTeleportation(worldServer, new BlockPos(location.pos)));
                            } else entity.changeDimension(dimension);

                            entity.setPositionAndUpdate(pos.getX() + 0.5D, pos.getY() + 1, pos.getZ() + 0.5D);
                            if (oldDimension == 1) {
                                entity.setPositionAndUpdate(pos.getX() + 0.5D, pos.getY() + 1, pos.getZ() + 0.5D);
                                world.spawnEntityInWorld(entity);
                                world.updateEntityWithOptionalForce(entity, false);
                            }

                            notteleported = false;
                        } else {
                            BlockPos pos = new BlockPos(location.pos);
                            if (world.isBlockLoaded(pos)) {
                                if (isValidLocation(world, pos)) {
                                    notteleported = false;
                                    entity.setPositionAndUpdate(pos.getX() + 0.5D, pos.getY() + 1, pos.getZ() + 0.5D);
                                }
                            }
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