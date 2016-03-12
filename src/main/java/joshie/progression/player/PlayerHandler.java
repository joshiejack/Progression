package joshie.progression.player;

import java.util.UUID;

import joshie.progression.api.IPlayerData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class PlayerHandler implements IPlayerData {
    @Override
    public NBTTagCompound getCustomData(EntityPlayer player, String key) {
        return PlayerTracker.getPlayerData(player).getAbilities().getCustomData(key);
    }

    @Override
    public NBTTagCompound getCustomData(UUID uuid, String key) {
        return PlayerTracker.getPlayerData(uuid).getAbilities().getCustomData(key);
    }

    @Override
    public void setCustomData(UUID uuid, String key, NBTTagCompound tag) {
        PlayerTracker.getServerPlayer(uuid).setCustomData(key, tag);
    }

    @Override
    public void setCustomData(EntityPlayer player, String key, NBTTagCompound tag) {
        PlayerTracker.getServerPlayer(player).setCustomData(key, tag);
    }
}
