package joshie.progression.player.data;

import java.util.HashMap;

import joshie.progression.helpers.NBTHelper;
import joshie.progression.network.core.PacketNBT.INBTWritable;
import joshie.progression.player.nbt.CustomNBT;
import net.minecraft.nbt.NBTTagCompound;

public class CustomStats implements INBTWritable<CustomStats> {
    private HashMap<String, NBTTagCompound> customData = new HashMap();

    public NBTTagCompound getCustomData(String key) {
        return customData.get(key);
    }

    public void setCustomData(String key, NBTTagCompound tag) {
        if (key == null || tag == null) return; //Don't add nulls
        customData.put(key, tag);
    }

    @Override
    public CustomStats readFromNBT(NBTTagCompound tag) {
        NBTHelper.readMap(tag, "Custom", CustomNBT.INSTANCE.setMap(customData));
        return this;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        NBTHelper.writeMap(tag, "Custom", CustomNBT.INSTANCE.setMap(customData));
        return tag;
    }
}
