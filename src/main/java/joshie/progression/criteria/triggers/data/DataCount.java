package joshie.progression.criteria.triggers.data;

import joshie.progression.api.criteria.IProgressionTriggerData;
import net.minecraft.nbt.NBTTagCompound;

public class DataCount implements IProgressionTriggerData {
	public int count = 0;

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		count = tag.getInteger("Count");
	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		tag.setInteger("Count", count);
	}
}
