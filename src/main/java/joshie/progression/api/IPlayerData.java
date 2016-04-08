package joshie.progression.api;

import joshie.progression.api.criteria.ICriteria;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Set;
import java.util.UUID;

public interface IPlayerData {
    /** Returns the tag compound with this string id **/
    public NBTTagCompound getCustomData(EntityPlayer player, String string);
    
    /** Returns the tag compound with this string id **/
    public NBTTagCompound getCustomData(UUID uuid, String string, boolean isClient);

    /** Returns a stored doubled with this name **/
    public double getDouble(UUID uuid, String name, boolean isClient);

    /** Get a boolean **/
    public boolean getBoolean(UUID uuid, String name, boolean isClient);
    
    
    /** Only call any of these setters serverside, client side will break **/
    public void setCustomData(UUID uuid, String string, NBTTagCompound tag);
    public void setCustomData(EntityPlayer player, String string, NBTTagCompound tag); 
    
    /** Set a boolean **/
    public void setBoolean(EntityPlayerMP player, String name, boolean value);
    /** Add to the the internal double stored with this name **/
    public void addDouble(EntityPlayerMP player, String name, double value);

    /** Returns a list of all the completed criteria for this player **/
    public Set<ICriteria> getCompletedCriteriaList(UUID uuid, boolean isClient);
}
