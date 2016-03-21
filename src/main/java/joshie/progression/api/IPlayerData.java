package joshie.progression.api;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public interface IPlayerData {
    /** Returns the tag compound with this string id **/
    public NBTTagCompound getCustomData(EntityPlayer player, String string);
    
    /** Returns the tag compound with this string id **/
    public NBTTagCompound getCustomData(UUID uuid, String string);

    /** Returns a stored doubled with this name **/
    public double getDouble(UUID uuid, String name);

    /** Get a boolean **/
    public boolean getBoolean(UUID uuid, String name);
    
    
    /** Only call any of these setters serverside, client side will break **/
    public void setCustomData(UUID uuid, String string, NBTTagCompound tag);
    public void setCustomData(EntityPlayer player, String string, NBTTagCompound tag); 
    
    /** Set a boolean **/
    public void setBoolean(UUID uuid, String name, boolean value);
    /** Add to the the internal double stored with this name **/
    public void addDouble(UUID uuid, String name, double value);
}
