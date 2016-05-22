package joshie.progression.criteria.rewards;

import joshie.progression.api.criteria.*;
import joshie.progression.api.special.*;
import joshie.progression.gui.fields.EntityFilterFieldPreview;
import joshie.progression.gui.fields.ItemFilterField;
import joshie.progression.gui.filters.FilterTypeEntity;
import joshie.progression.gui.filters.FilterTypeLocation;
import joshie.progression.helpers.EntityHelper;
import joshie.progression.helpers.MCClientHelper;
import joshie.progression.helpers.StackHelper;
import joshie.progression.lib.WorldLocation;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.lang3.text.WordUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.minecraft.util.text.TextFormatting.DARK_GREEN;

@ProgressionRule(name="entity", color=0xFFE599FF)
public class RewardSpawnEntity extends RewardBase implements IInit, ICustomDescription, ICustomTooltip, ICustomWidth, ICustomIcon, IHasFilters, ISpecialFieldProvider {
    public List<IFilterProvider> locations = new ArrayList();
    public List<IFilterProvider> entities = new ArrayList();
    public NBTTagCompound tagValue = new NBTTagCompound();
    public int spawnNumber = 1;
    public String nbtData = "";

    protected transient IField field;
    protected transient EntityLivingBase entity;
    protected transient int ticker;

    public RewardSpawnEntity() {
        field = new ItemFilterField("locations", this);
    }

    @Override
    public void init(boolean isClient) {
        tagValue = StackHelper.getTag(new String[] { nbtData }, 0);
    }

    @Override
    public String getDescription() {
        return format(spawnNumber) + " \n" + field.getField();
    }

    @Override
    public void addTooltip(List list) {
        list.add(DARK_GREEN + format(spawnNumber));
        list.addAll(Arrays.asList(WordUtils.wrap((String)field.getField(), 28).split("\r\n")));
        ItemStack stack = getIcon();
        if (stack != null) {
            list.add("---");
            list.add(entity.getName());
        }
    }

    @Override
    public int getWidth(DisplayMode mode) {
        return mode == DisplayMode.DISPLAY ? 121: 100;
    }

    @Override
    public ItemStack getIcon() {
        return EntityHelper.getItemForEntity(getEntity());
    }

    @Override
    public List<IFilterProvider> getAllFilters() {
        ArrayList<IFilterProvider> all = new ArrayList();
        all.addAll(locations);
        all.addAll(entities);
        return all;
    }

    @Override
    public IFilterType getFilterForField(String fieldName) {
        if (fieldName.equals("locations")) return FilterTypeLocation.INSTANCE;
        if (fieldName.equals("entities")) return FilterTypeEntity.INSTANCE;

        return null;
    }

    @Override
    public void addSpecialFields(List<IField> fields, DisplayMode mode) {
        if (mode == DisplayMode.EDIT) {
            fields.add(new ItemFilterField("locations", this));
            fields.add(new ItemFilterField("entities", this));
        } else fields.add(new EntityFilterFieldPreview("entities", this, 45, 70, 2.8F));
    }

    @Override
    public void reward(EntityPlayerMP player) {
        if (player != null) {
            for (int i = 0; i < spawnNumber; i++) {
                boolean notspawned = true;
                for (int j = 0; j < 10 && notspawned; j++) {
                    WorldLocation location = WorldLocation.getRandomLocationFromFilters(locations, player);
                    if (location != null) {
                        BlockPos pos = new BlockPos(location.pos);
                        if (player.worldObj.isBlockLoaded(pos)) {
                            if (isValidLocation(player.worldObj, pos)) {
                                pos = pos.up();

                                notspawned = false;
                                //Now that we have a random location, let's grab a random Entity
                                IFilter filter = EntityHelper.getFilter(entities, player);
                                EntityLivingBase clone = (EntityLivingBase) EntityList.createEntityByName(EntityHelper.getNameForEntity(((EntityLivingBase) filter.getRandom(player))), player.worldObj);
                                if (clone instanceof EntityLiving) {
                                    ((EntityLiving) clone).onInitialSpawn(player.worldObj.getDifficultyForLocation(new BlockPos(clone)), (IEntityLivingData) null);
                                }

                                if (tagValue != null) {
                                    clone.readEntityFromNBT(tagValue);
                                }

                                filter.apply(entity);

                                clone.setLocationAndAngles(pos.getX(), pos.getY(), pos.getZ(), player.worldObj.rand.nextFloat() * 360.0F, 0.0F);
                                player.worldObj.spawnEntityInWorld(clone);
                                player.worldObj.playEvent(2004, pos, 0);
                                if (clone instanceof EntityLiving) {
                                    ((EntityLiving) clone).spawnExplosionParticle();
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

    private EntityLivingBase getEntity() {
        if (ticker >= 200 || ticker == 0) {
            entity = EntityHelper.getRandomEntityFromFilters(entities, MCClientHelper.getPlayer());
            ticker = 1;
        }

        if (!GuiScreen.isShiftKeyDown()) ticker++;
        return entity != null ? entity : MCClientHelper.getPlayer();
    }
}
