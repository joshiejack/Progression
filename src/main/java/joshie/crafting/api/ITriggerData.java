package joshie.crafting.api;

import net.minecraft.nbt.NBTTagCompound;

public interface ITriggerData {
	/** Reads extra data from nbt **/
	public void readFromNBT(NBTTagCompound tag);
	
	/** Writes extra data to nbt **/
	public void writeToNBT(NBTTagCompound tag);
}
