package joshie.progression.api;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public interface IPlayerData {
    public NBTTagCompound getCustomData(EntityPlayer player, String string);
    public NBTTagCompound getCustomData(UUID uuid, String string);
    
    /** Only call me serverside **/
    public void setCustomData(UUID uuid, String string, NBTTagCompound tag);
    /** Only call me serverside **/
    public void setCustomData(EntityPlayer player, String string, NBTTagCompound tag); 
}
