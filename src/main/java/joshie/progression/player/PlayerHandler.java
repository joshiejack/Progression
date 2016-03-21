package joshie.progression.player;

import java.util.UUID;

import joshie.progression.api.IPlayerData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

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
    public void addDouble(UUID uuid, String name, double value) {
        PlayerTracker.getServerPlayer(uuid).addDouble(name, value);
    }

    @Override
    public double getDouble(UUID uuid, String name) {
        return PlayerTracker.getPlayerData(uuid).getPoints().getDouble(name);
    }

    @Override
    public void setBoolean(UUID uuid, String name, boolean value) {
        PlayerTracker.getServerPlayer(uuid).setBoolean(name, value);
    }

    @Override
    public boolean getBoolean(UUID uuid, String name) {
        return PlayerTracker.getPlayerData(uuid).getPoints().getBoolean(name);
    }
}
