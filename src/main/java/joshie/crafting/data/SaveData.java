package joshie.crafting.data;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldSavedData;

public class SaveData extends WorldSavedData {	
	public SaveData(String string) {
		super(string);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		DataServer.INSTANCE.readFromNBT(nbt);
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		DataServer.INSTANCE.writeToNBT(nbt);
	}
}
