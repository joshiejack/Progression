package joshie.progression.criteria.rewards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import joshie.progression.api.IField;
import joshie.progression.api.IFilter;
import joshie.progression.api.ISetterCallback;
import joshie.progression.api.ISpecialFilters;
import joshie.progression.api.gui.ISpecialFieldProvider;
import joshie.progression.gui.newversion.overlays.IFilterSelectorFilter;
import joshie.progression.gui.selector.filters.EntityFilter;
import joshie.progression.gui.selector.filters.LocationFilter;
import joshie.progression.helpers.EntityHelper;
import joshie.progression.helpers.PlayerHelper;
import joshie.progression.helpers.StackHelper;
import joshie.progression.lib.WorldLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

public class RewardSpawnEntity extends RewardBase implements ISpecialFieldProvider, ISpecialFilters, ISetterCallback {
    public List<IFilter> locations = new ArrayList();
    public List<IFilter> entities = new ArrayList();
    public NBTTagCompound tagValue = new NBTTagCompound();
    public String tagText = "";
    public int spawnNumber = 1;

    public RewardSpawnEntity() {
        super("entity", 0xFFE599FF);
    }

    @Override
    public void addSpecialFields(List<IField> fields) {
        //fields.add(new ItemFilterFieldPreview("filters", this, 25, 30, 26, 70, 25, 75, 2.8F));
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
                        WorldLocation location = (WorldLocation) locality.get(0);
                        //Now that we have a random location, let's grab a random Entity
                        EntityLivingBase entity = EntityHelper.getRandomEntityForFilters(entities);
                        //EntityLivingBase clone = EntityList.createEntityByName(entityName, worldIn)
                    }
                }

                // ItemStack stack = ItemHelper.getRandomItemOfSize(filters, stackSize);
                //PacketHandler.sendToClient(new PacketRewardItem(stack.copy()), (EntityPlayerMP) player);
                //SpawnItemHelper.addToPlayerInventory(player, stack.copy());
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
