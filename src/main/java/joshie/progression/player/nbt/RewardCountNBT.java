package joshie.progression.player.nbt;

import joshie.progression.helpers.NBTHelper;
import joshie.progression.helpers.NBTHelper.IMapHelper;
import net.minecraft.nbt.NBTTagCompound;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RewardCountNBT implements IMapHelper {
    public static final RewardCountNBT INSTANCE = new RewardCountNBT();

    public HashMap map;

    public IMapHelper setMap(HashMap map) {
        this.map = map;
        return this;
    }

    @Override
    public Map getMap() {
        return map;
    }

    @Override
    public Object readKey(NBTTagCompound tag) {
        String name = tag.getString("UUID");
        return UUID.fromString(name);
    }

    @Override
    public Object readValue(NBTTagCompound tag) {
        NBTTagCompound data = tag.getCompoundTag("Data");
        HashMap map = new HashMap();
        NBTHelper.readMap(data, "Criteria Reward Counter", CriteriaNBT.INSTANCE.setMap(map));
        return map;
    }

    @Override
    public void writeKey(NBTTagCompound tag, Object o) {
        String name = ((UUID)o).toString();
        tag.setString("UUID", name);
    }

    @Override
    public void writeValue(NBTTagCompound tag, Object o) {
        NBTTagCompound data = new NBTTagCompound();
        NBTHelper.writeMap(data, "Criteria Reward Counter", CriteriaNBT.INSTANCE.setMap((HashMap)o));
        tag.setTag("Data", data);
    }
}
