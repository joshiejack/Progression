package joshie.progression.plugins.enchiridion.rewards;

import joshie.enchiridion.api.event.FeatureVisibleEvent;
import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.special.IStoreNBTData;
import joshie.progression.criteria.rewards.RewardBaseAbility;
import joshie.progression.items.ItemCriteria;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class RewardShowLayer extends RewardBaseAbility implements IStoreNBTData {
    public boolean hideByDefault = true;
    public String bookid = "";
    public int page = 1;
    public int layer = 1;

    public RewardShowLayer() {
        super(ItemCriteria.getStackFromMeta(ItemCriteria.ItemMeta.showLayer), "layer.show", 0xFFCCCCCC);
    }

    @SubscribeEvent
    public void onFeatureRender(FeatureVisibleEvent event) {
        NBTTagCompound tag = ProgressionAPI.player.getCustomData(event.entityPlayer, "enchiridion.hidden");
        if (tag != null) {
            if (tag.hasKey(event.bookid)) {
                NBTTagCompound bookData = tag.getCompoundTag(event.bookid);
                if (bookData.hasKey("" + event.page)) {
                    NBTTagCompound pageData = bookData.getCompoundTag("" + event.page);
                    if (pageData.hasKey("" + event.layer)) event.setCanceled(true);
                }
            }
        }
    }

    public NBTTagCompound getTag(NBTTagCompound tag, String name) {
        if (tag.hasKey(name)) return tag.getCompoundTag(name);
        NBTTagCompound nbt = new NBTTagCompound();
        tag.setTag(name, nbt);
        return nbt;
    }

    @Override
    public String getNBTKey() {
        return "enchiridion.hidden";
    }

    @Override
    public NBTTagCompound getDefaultTags(NBTTagCompound tag) {
        if (hideByDefault) {
            NBTTagCompound bookData = getTag(tag, bookid);
            NBTTagCompound pageData = getTag(bookData, "" + page);
            pageData.setBoolean("" + layer, true);
        }

        return tag;
    }

    @Override
    public void reward(EntityPlayerMP player) {
        NBTTagCompound tag = ProgressionAPI.player.getCustomData(player, "enchiridion.hidden");
        if (tag == null) tag = new NBTTagCompound();
        NBTTagCompound bookData = getTag(tag, bookid);
        NBTTagCompound pageData = getTag(bookData, "" + page);
        if (hideByDefault) pageData.removeTag("" + layer);
        else pageData.setBoolean("" + layer, true);

        ProgressionAPI.player.setCustomData(player, "enchiridion.hidden", tag);
    }
}
