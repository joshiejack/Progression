package joshie.progression.player;

import joshie.progression.api.IPlayerData;
import joshie.progression.api.criteria.ICriteria;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Set;
import java.util.UUID;

public class PlayerHandler implements IPlayerData {
    @Override
    public NBTTagCompound getCustomData(EntityPlayer player, String key) {
        return PlayerTracker.getPlayerData(player).getCustomStats().getCustomData(key);
    }

    @Override
    public NBTTagCompound getCustomData(UUID uuid, String key) {
        return PlayerTracker.getPlayerData(uuid).getCustomStats().getCustomData(key);
    }

    @Override
    public void setCustomData(UUID uuid, String key, NBTTagCompound tag) {
        PlayerTracker.getServerPlayer(uuid).setCustomData(key, tag);
    }

    @Override
    public void setCustomData(EntityPlayer player, String key, NBTTagCompound tag) {
        PlayerTracker.getServerPlayer(player).setCustomData(key, tag);
    }

    @Override
    public void addDouble(EntityPlayerMP player, String name, double value) {
        PlayerTracker.getServerPlayer(player).addDouble(name, value);
    }

    @Override
    public Set<ICriteria> getCompletedCriteriaList(UUID uuid) {
        return PlayerTracker.getPlayerData(uuid).getMappings().getCompletedCriteria().keySet();
    }

    @Override
    public double getDouble(UUID uuid, String name) {
        return PlayerTracker.getPlayerData(uuid).getPoints().getDouble(name);
    }

    @Override
    public void setBoolean(EntityPlayerMP player, String name, boolean value) {
        PlayerTracker.getServerPlayer(player).setBoolean(name, value);
    }

    @Override
    public boolean getBoolean(UUID uuid, String name) {
        return PlayerTracker.getPlayerData(uuid).getPoints().getBoolean(name);
    }
}
