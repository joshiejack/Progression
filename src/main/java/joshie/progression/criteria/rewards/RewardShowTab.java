package joshie.progression.criteria.rewards;

import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.criteria.IProgressionTab;
import joshie.progression.api.event.TabVisibleEvent;
import joshie.progression.api.special.IGetterCallback;
import joshie.progression.api.special.IHasEventBus;
import joshie.progression.api.special.IInit;
import joshie.progression.api.special.IStoreNBTData;
import joshie.progression.handlers.APIHandler;
import joshie.progression.items.ItemCriteria;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.UUID;

public class RewardShowTab extends RewardBaseAbility implements IStoreNBTData, IHasEventBus, IInit, IGetterCallback {
    public boolean hideByDefault = true;
    public String displayName = "";
    private UUID tabID = UUID.randomUUID();
    private IProgressionTab tab;

    public RewardShowTab() {
        super(ItemCriteria.getStackFromMeta(ItemCriteria.ItemMeta.showTab), "tab.show", 0xFFCCCCCC);
    }

    @Override
    public void init() {
        try {
            for (IProgressionTab t : APIHandler.getTabs().values()) {
                String display = t.getDisplayName();
                if (t.getDisplayName().equals(displayName)) {
                    tab = t;
                    tabID = t.getUniqueID();
                    break;
                }
            }
        } catch (Exception e) {}
    }

    public IProgressionTab getAssignedTab() {
        return APIHandler.getTabFromName(tabID);
    }

    @SubscribeEvent
    public void onFeatureRender(TabVisibleEvent event) {
        NBTTagCompound tag = ProgressionAPI.player.getCustomData(event.entityPlayer, "progression.tab.hidden");
        if (tag != null) {
            if (tag.hasKey(event.unique.toString())) {
                event.setCanceled(true);
            }
        }
    }

    @Override
    public String getNBTKey() {
        return "progression.tab.hidden";
    }

    @Override
    public NBTTagCompound getDefaultTags(NBTTagCompound tag) {
        if (hideByDefault) {
            if (tab != null) {
                tag.setBoolean(tab.getUniqueID().toString(), true);
            }
        }

        return tag;
    }

    @Override
    public String getField(String fieldName) {
        return tab != null ? EnumChatFormatting.GREEN + displayName : EnumChatFormatting.RED + displayName;
    }

    @Override
    public void reward(EntityPlayerMP player) {
        if (tab == null) return; //Do not give the reward

        NBTTagCompound tag = ProgressionAPI.player.getCustomData(player, "progression.tab.hidden");
        if (tag == null) tag = new NBTTagCompound();
        if (hideByDefault) tag.removeTag(tab.getUniqueID().toString());
        else tag.setBoolean(tab.getUniqueID().toString(), true);

        ProgressionAPI.player.setCustomData(player, "progression.tab.hidden", tag);
    }

}
