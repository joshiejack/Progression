package joshie.progression.criteria.rewards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import joshie.progression.api.IFilter;
import joshie.progression.api.ISetterCallback;
import joshie.progression.api.ISpecialFilters;
import joshie.progression.api.fields.IField;
import joshie.progression.api.fields.ISpecialFieldProvider;
import joshie.progression.api.filters.IFilterSelectorFilter;
import joshie.progression.gui.selector.filters.EntityFilter;
import joshie.progression.gui.selector.filters.LocationFilter;
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

public class RewardSpawnEntity extends RewardBase implements ISpecialFilters, ISetterCallback {
    public List<IFilter> locations = new ArrayList();
    public List<IFilter> entities = new ArrayList();
    public NBTTagCompound tagValue = new NBTTagCompound();
    public String tagText = "";
    public int spawnNumber = 1;
    public boolean armour = true;

    public RewardSpawnEntity() {
        super("entity", 0xFFE599FF);
    }
    
    @Override
    public void reward(UUID uuid) {
        List<EntityPlayerMP> players = PlayerHelper.getPlayersFromUUID(uuid);
        for (EntityPlayerMP player : players) {
            if (player != null) {
                for (int i = 0; i < spawnNumber; i++) {
                    ArrayList<IFilter> locality = new ArrayList(locations);
                    if (locality.size() > 0) {
                        Collections.shuffle(locality);
                        WorldLocation location = (WorldLocation) locality.get(0).getMatches(player).get(0);
                        //Now that we have a random location, let's grab a random Entity
                        EntityLivingBase entity = EntityHelper.getRandomEntityForFilters(entities);
                        EntityLivingBase clone = (EntityLivingBase) EntityList.createEntityByName(EntityHelper.getNameForEntity(entity), player.worldObj);
                        if (clone instanceof EntityLiving) {
                            System.out.println("DOING THE SPAWN STUFF");
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
    public IFilterSelectorFilter getFilterForField(String fieldName) {
        if (fieldName.equals("locations")) return LocationFilter.INSTANCE;
        if (fieldName.equals("entities")) return EntityFilter.INSTANCE;

        return null;
    }

    @Override
    public boolean setField(String fieldName, Object object) {
        if (fieldName.equals("tagText")) {
            String fieldValue = (String) object;
            tagValue = StackHelper.getTag(new String[] { fieldValue }, 0);
            tagText = fieldValue; //Temporary fieldr
            return true;
        }

        return false;
    }
}
