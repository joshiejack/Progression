package joshie.crafting.trigger.data;

import joshie.crafting.api.ITriggerData;
import net.minecraft.nbt.NBTTagCompound;

public class DataBoolean implements ITriggerData {
	public boolean completed = false;

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		completed = tag.getBoolean("Completed");
	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		tag.setBoolean("Completed", completed);
	}
}
