package joshie.progression.criteria.rewards;

import joshie.progression.Progression;
import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.criteria.ITab;
import joshie.progression.api.criteria.ProgressionRule;
import joshie.progression.api.event.TabVisibleEvent;
import joshie.progression.api.special.*;
import joshie.progression.handlers.APIHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.UUID;

@ProgressionRule(name="tab.show", color=0xFFCCCCCC, meta="showTab")
public class RewardShowTab extends RewardBaseSingular implements IInit, IGetterCallback, IStoreNBTData, IHasEventBus  {
    public boolean hideByDefault = true;
    public String displayName = "";
    private UUID tabID = UUID.randomUUID();
    private ITab tab;

    @Override
    public void init() {
        try {
            for (ITab t : APIHandler.getTabs().values()) {
                String display = t.getDisplayName();
                if (t.getDisplayName().equals(displayName)) {
                    tab = t;
                    tabID = t.getUniqueID();
                    break;
                }
            }
        } catch (Exception e) {}
    }


    @Override
    public String getDescription() {
        if (tab != null) {
            String end = hideByDefault ? "show" : "hide";
            return Progression.format("reward.layer.show.description." + end, tab.getDisplayName());
        } else return "Invalid Book setup";
    }

    @Override
    public String getField(String fieldName) {
        return tab != null ? EnumChatFormatting.GREEN + displayName : EnumChatFormatting.RED + displayName;
    }

    @Override
    public EventBus getEventBus() {
        return MinecraftForge.EVENT_BUS;
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
    public void reward(EntityPlayerMP player) {
        if (tab == null) return; //Do not give the reward

        NBTTagCompound tag = ProgressionAPI.player.getCustomData(player, "progression.tab.hidden");
        if (tag == null) tag = new NBTTagCompound();
        if (hideByDefault) tag.removeTag(tab.getUniqueID().toString());
        else tag.setBoolean(tab.getUniqueID().toString(), true);

        ProgressionAPI.player.setCustomData(player, "progression.tab.hidden", tag);
    }
}
