package joshie.progression.criteria.rewards;

import com.google.gson.JsonObject;
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
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
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
public class RewardSpawnEntity extends RewardBase implements IInit, ICustomDescription, ICustomTooltip, ICustomWidth, ICustomIcon, IHasFilters, ISpecialFieldProvider, ISpecialJSON {
    public List<IFilterProvider> locations = new ArrayList();
    public List<IFilterProvider> entities = new ArrayList();
    public NBTTagCompound tagValue = new NBTTagCompound();
    public int spawnNumberMin = 1;
    public int spawnNumberMax = 1;
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
        return format(spawnNumberMin, spawnNumberMax) + " \n" + field.getField();
    }

    @Override
    public void addTooltip(List list) {
        list.add(DARK_GREEN + format(spawnNumberMin, spawnNumberMax));
        list.addAll(Arrays.asList(WordUtils.wrap((String)field.getField(), 28).split("\r\n")));
        ItemStack stack = getIcon();
        if (stack != null && entity != null) {
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
            int random = Math.max(0, (spawnNumberMax - spawnNumberMin));
            int additional = 0;
            if (random != 0) {
                additional = player.worldObj.rand.nextInt(random + 1);
            }

            int spawnNumber = spawnNumberMin + additional;
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
                                if (filter != null) {
                                    List<EntityLivingBase> entities = (List<EntityLivingBase>) filter.getRandom(player);
                                    for (EntityLivingBase entity : entities) {
                                        EntityLivingBase clone = EntityHelper.clone(player.worldObj, entity, tagValue, filter);
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
        }
    }

    @Override
    public boolean onlySpecial() {
        return false;
    }

    @Override
    public void readFromJSON(JsonObject data) {
        if (data.get("spawnNumber") != null) {
            spawnNumberMin = data.get("spawnNumber").getAsInt();
            spawnNumberMax = data.get("spawnNumber").getAsInt();
        }
    }

    @Override
    public void writeToJSON(JsonObject object) {}

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