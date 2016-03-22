package joshie.progression.criteria.rewards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import joshie.progression.api.criteria.IProgressionFilter;
import joshie.progression.api.criteria.IProgressionFilterSelector;
import joshie.progression.api.special.IHasFilters;
import joshie.progression.api.special.IInit;
import joshie.progression.api.special.ISpecialFilters;
import joshie.progression.gui.filters.FilterSelectorEntity;
import joshie.progression.gui.filters.FilterSelectorLocation;
import joshie.progression.helpers.EntityHelper;
import joshie.progression.helpers.PlayerHelper;
import joshie.progression.helpers.StackHelper;
import joshie.progression.lib.WorldLocation;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;

public class RewardSpawnEntity extends RewardBase implements ISpecialFilters, IInit, IHasFilters {
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
    public List<IProgressionFilter> getAllFilters() {
        ArrayList<IProgressionFilter> all = new ArrayList();
        all.addAll(locations);
        all.addAll(entities);
        return all;
    }
    
    @Override
    public void reward(UUID uuid) {
        List<EntityPlayerMP> players = PlayerHelper.getPlayersFromUUID(uuid);
        for (EntityPlayerMP player : players) {
            if (player != null) {
                for (int i = 0; i < spawnNumber; i++) {
                    ArrayList<IProgressionFilter> locality = new ArrayList(locations);
                    if (locality.size() > 0) {
                        Collections.shuffle(locality);
                        WorldLocation location = (WorldLocation) locality.get(0).getMatches(player).get(0);
                        //Now that we have a random location, let's grab a random Entity
                        EntityLivingBase entity = EntityHelper.getRandomEntityForFilters(entities);
                        EntityLivingBase clone = (EntityLivingBase) EntityList.createEntityByName(EntityHelper.getNameForEntity(entity), player.worldObj);
                        if (clone instanceof EntityLiving) {
                            ((EntityLiving) clone).onInitialSpawn(player.worldObj.getDifficultyForLocation(new BlockPos(clone)), (IEntityLivingData) null);
                        }

                        clone.setLocationAndAngles(location.pos.getX(), location.pos.getY(), location.pos.getZ(), player.worldObj.rand.nextFloat() * 360.0F, 0.0F);
                        player.worldObj.spawnEntityInWorld(clone);
                        player.worldObj.playAuxSFX(2004, location.pos, 0);
                        if (clone instanceof EntityLiving) {
                            ((EntityLiving) clone).spawnExplosionParticle();
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
