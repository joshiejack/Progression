package joshie.crafting.trigger.data;

import joshie.crafting.api.ITriggerData;
import net.minecraft.nbt.NBTTagCompound;

public class DataCrafting implements ITriggerData {
	public int amountCrafted = 0;
	public int timesCrafted = 0;

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		amountCrafted = tag.getInteger("AmountCrafted");
		timesCrafted = tag.getInteger("TimesCrafted");
	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		tag.setInteger("AmountCrafted", amountCrafted);
		tag.setInteger("TimesCrafted", timesCrafted);
	}
}
