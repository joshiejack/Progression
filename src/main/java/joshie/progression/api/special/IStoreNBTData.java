package joshie.progression.api.special;

import net.minecraft.nbt.NBTTagCompound;

/** Adding this interface to rewards will allow them to save and load
 *  nbt data to players. */
public interface IStoreNBTData {
    /** Return the name to save nbt data under **/
    public String getNBTKey();

    /** Add default tags **/
    public NBTTagCompound getDefaultTags(NBTTagCompound tag);
}
