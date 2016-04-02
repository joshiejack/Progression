package joshie.progression.plugins.enchiridion.rewards;

import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.book.IBook;
import joshie.enchiridion.api.event.FeatureVisibleEvent;
import joshie.progression.Progression;
import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.special.IGetterCallback;
import joshie.progression.api.special.IInit;
import joshie.progression.api.special.IStoreNBTData;
import joshie.progression.criteria.rewards.RewardBaseAbility;
import joshie.progression.items.ItemCriteria;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class RewardShowLayer extends RewardBaseAbility implements IStoreNBTData, IInit, IGetterCallback {
    private transient IBook theBook;
    public boolean hideByDefault = true;
    public String bookid = "";
    public int page = 1;
    public int layer = 1;

    public RewardShowLayer() {
        super(ItemCriteria.getStackFromMeta(ItemCriteria.ItemMeta.showLayer), "layer.show", 0xFFCCCCCC);
    }

    @Override
    public void init() {
        theBook = EnchiridionAPI.instance.getBook(bookid);
    }

    @Override
    public String getField(String fieldName) {
        if (fieldName.equals("layer")) return "" + layer;
        else if (fieldName.equals("page")) return "" + page;
        else return theBook != null ? EnumChatFormatting.GREEN + bookid : EnumChatFormatting.RED + bookid;
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

    @Override
    public String getDescription() {
        if (theBook != null) {
            String end = hideByDefault ? "show" : "hide";
            return Progression.format("reward.layer.show.description." + end, theBook.getDisplayName(), page);
        } else return "Invalid Book setup";
    }
}
