package joshie.progression.enchiridion.rewards;

import java.util.List;
import java.util.UUID;

import com.google.gson.JsonObject;

import joshie.enchiridion.api.event.FeatureVisibleEvent;
import joshie.progression.api.EventBusType;
import joshie.progression.api.ProgressionAPI;
import joshie.progression.criteria.rewards.RewardBase;
import joshie.progression.gui.fields.BooleanField;
import joshie.progression.gui.fields.TextField;
import joshie.progression.helpers.JSONHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class RewardShowLayer extends RewardBase {
    public boolean hideByDefault = true;
    public String bookid = "";
    public int page = 1;
    public int layer = 1;

    public RewardShowLayer() {
        super("layer.show", 0xFFCCCCCC);
        list.add(new BooleanField("hideByDefault", this));
        list.add(new TextField("bookid", this));
        list.add(new TextField("page", this));
        list.add(new TextField("layer", this));
    }

    @Override
    public EventBusType getEventBus() {
        return EventBusType.FORGE;
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
    public void reward(UUID uuid) {
        NBTTagCompound tag = ProgressionAPI.player.getCustomData(uuid, "enchiridion.hidden");
        if (tag == null) tag = new NBTTagCompound();
        NBTTagCompound bookData = getTag(tag, bookid);
        NBTTagCompound pageData = getTag(bookData, "" + page);
        if (hideByDefault) pageData.removeTag("" + layer);
        else pageData.setBoolean("" + layer, true);
        
        ProgressionAPI.player.setCustomData(uuid, "enchiridion.hidden", tag);
    }

    @Override
    public void addTooltip(List list) {}

    @Override
    public void readFromJSON(JsonObject object) {
        hideByDefault = JSONHelper.getBoolean(object, "hideByDefault", true);
        bookid = JSONHelper.getString(object, "bookid", "");
        page = JSONHelper.getInteger(object, "page", 1);
        layer = JSONHelper.getInteger(object, "layer", 0);
    }

    @Override
    public void writeToJSON(JsonObject object) {
        JSONHelper.setBoolean(object, "hideByDefault", hideByDefault, true);
        JSONHelper.setString(object, "bookid", bookid, "");
        JSONHelper.setInteger(object, "page", page, 1);
        JSONHelper.setInteger(object, "layer", layer, 1);
    }
}
