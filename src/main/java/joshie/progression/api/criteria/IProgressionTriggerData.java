package joshie.progression.api.criteria;

import net.minecraft.nbt.NBTTagCompound;

public interface IProgressionTriggerData {    
	/** Reads extra data from nbt **/
	public void readFromNBT(NBTTagCompound tag);
	
	/** Writes extra data to nbt **/
	public void writeToNBT(NBTTagCompound tag);
}
