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
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
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
    public boolean defaultToPlayer = true;
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
                    if (entities.size() == 0 && defaultToPlayer) entities.add(thePlayer);
                    for (EntityLivingBase entity : entities) {
                        World world = DimensionManager.getWorld(location.dimension);
                        int dimension = location.dimension;
                        if (world == null) continue; //NO!!!!
                        if (entity.dimension != dimension) { //From RFTools
                            MinecraftServer server = MinecraftServer.getServer();
                            WorldServer worldServer = server.worldServerForDimension(dimension);
                            int oldDimension = entity.worldObj.provider.getDimensionId();
                            BlockPos pos = new BlockPos(location.pos);
                            if (entity instanceof EntityPlayer) {
                                ((EntityPlayerMP) entity).addExperienceLevel(0); //Fix levels
                                worldServer.getMinecraftServer().getConfigurationManager().transferPlayerToDimension(((EntityPlayerMP) entity), dimension, new DimensionTeleportation(worldServer, new BlockPos(location.pos)));
                            } else entity.travelToDimension(dimension);

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
        Material posfloor = world.getBlockState(pos).getBlock().getMaterial();
        Material posfeet = world.getBlockState(pos.up()).getBlock().getMaterial();
        Material poshead = world.getBlockState(pos.up(2)).getBlock().getMaterial();
        if (posfeet.blocksMovement()) return false;
        if (poshead.blocksMovement()) return false;
        if (posfloor.isLiquid() || posfeet.isLiquid() || poshead.isLiquid()) return false;
        return posfloor.blocksMovement();
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
