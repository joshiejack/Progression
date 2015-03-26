package joshie.crafting.player.nbt;

import java.util.Map;

import joshie.crafting.helpers.NBTHelper.IMapHelper;
import net.minecraft.nbt.NBTTagCompound;

public class PointsNBT implements IMapHelper {
    public static final PointsNBT INSTANCE = new PointsNBT();
    
    public Map map;
    
    public IMapHelper setMap(Map map) {
        this.map = map;
        return this;
    }

    @Override
    public Map getMap() {
        return map;
    }

    @Override
    public Object readKey(NBTTagCompound tag) {
        return (String)tag.getString("Name");
    }

    @Override
    public Object readValue(NBTTagCompound tag) {
        return (Integer)tag.getInteger("Count");
    }

    @Override
    public void writeKey(NBTTagCompound tag, Object o) {
        tag.setString("Name", (String)o);
    }

    @Override
    public void writeValue(NBTTagCompound tag, Object o) {
        tag.setInteger("Count", (Integer)o);
    }
}
