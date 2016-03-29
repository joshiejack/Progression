package joshie.progression.criteria.rewards;

import joshie.progression.api.criteria.IProgressionField;
import joshie.progression.api.criteria.IProgressionFilter;
import joshie.progression.api.criteria.IProgressionFilterSelector;
import joshie.progression.api.special.IHasFilters;
import joshie.progression.api.special.IInit;
import joshie.progression.api.special.ISpecialFieldProvider;
import joshie.progression.api.special.ISpecialFilters;
import joshie.progression.gui.fields.EntityFilterFieldPreview;
import joshie.progression.gui.fields.ItemFilterField;
import joshie.progression.gui.filters.FilterSelectorEntity;
import joshie.progression.gui.filters.FilterSelectorLocation;
import joshie.progression.helpers.EntityHelper;
import joshie.progression.helpers.StackHelper;
import joshie.progression.lib.WorldLocation;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RewardSpawnEntity extends RewardBase implements ISpecialFilters, IInit, IHasFilters, ISpecialFieldProvider {
    public List<IProgressionFilter> locations = new ArrayList();
    public List<IProgressionFilter> entities = new ArrayList();
    public NBTTagCompound tagValue = new NBTTagCompound();
    public int spawnNumber = 1;
    public String nbtData = "";

    public RewardSpawnEntity() {
        super("entity", 0xFFE599FF);
    }
    
    @Override
    public void init() {
        tagValue = StackHelper.getTag(new String[] { nbtData }, 0);
    }

    @Override
    public void addSpecialFields(List<IProgressionField> fields, DisplayMode mode) {
        if (mode == DisplayMode.EDIT) {
            fields.add(new ItemFilterField("locations", this));
            fields.add(new ItemFilterField("entities", this));
        } else fields.add(new EntityFilterFieldPreview("entities", this, 15, 70, 2.8F));
    }
    
    @Override
    public List<IProgressionFilter> getAllFilters() {
        ArrayList<IProgressionFilter> all = new ArrayList();
        all.addAll(locations);
        all.addAll(entities);
        return all;
    }

    private boolean isValidLocation(World world, BlockPos pos) {
        Material posfloor = world.getBlockState(pos).getBlock().getMaterial();
        Material posfeet = world.getBlockState(pos.up()).getBlock().getMaterial();
        Material poshead = world.getBlockState(pos.up(2)).getBlock().getMaterial();
        if (posfeet.blocksMovement()) return false;
        if (poshead.blocksMovement()) return false;
        if (posfloor.isLiquid() || posfeet.isLiquid() || poshead.isLiquid()) return false;
        return posfloor.blocksMovement();
    }
    
    @Override
    public void reward(EntityPlayerMP player) {
        if (player != null) {
            for (int i = 0; i < spawnNumber; i++) {
                boolean notspawned = true;
                for (int j = 0; j < 10 && notspawned; j++) {
                    ArrayList<IProgressionFilter> locality = new ArrayList(locations);
                    if (locality.size() > 0) {
                        Collections.shuffle(locality);
                        WorldLocation location = (WorldLocation) locality.get(0).getMatches(player).get(0);
                        BlockPos pos = new BlockPos(location.pos);
                        if (player.worldObj.isBlockLoaded(pos)) {
                            if (isValidLocation(player.worldObj, pos)) {
                                notspawned = false;
                                //Now that we have a random location, let's grab a random Entity
                                EntityLivingBase entity = EntityHelper.getRandomEntityForFilters(entities);
                                EntityLivingBase clone = (EntityLivingBase) EntityList.createEntityByName(EntityHelper.getNameForEntity(entity), player.worldObj);
                                if (clone instanceof EntityLiving) {
                                    ((EntityLiving) clone).onInitialSpawn(player.worldObj.getDifficultyForLocation(new BlockPos(clone)), (IEntityLivingData) null);
                                }

                                clone.setLocationAndAngles(pos.getX(), pos.getY(), pos.getZ(), player.worldObj.rand.nextFloat() * 360.0F, 0.0F);
                                player.worldObj.spawnEntityInWorld(clone);
                                player.worldObj.playAuxSFX(2004, pos, 0);
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

    @Override
    public void addTooltip(List list) {
        // list.add(EnumChatFormatting.WHITE + Progression.translate("item.free"));
        // list.add(getIcon().getDisplayName() + " x" + spawnNumber);
    }

    @Override
    public IProgressionFilterSelector getFilterForField(String fieldName) {
        if (fieldName.equals("locations")) return FilterSelectorLocation.INSTANCE;
        if (fieldName.equals("entities")) return FilterSelectorEntity.INSTANCE;

        return null;
    }
}
