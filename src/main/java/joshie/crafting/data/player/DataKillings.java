package joshie.crafting.data.player;

import java.util.HashMap;

import net.minecraft.nbt.NBTTagCompound;

public class DataKillings {
	private HashMap<String, Integer> killings = new HashMap();
	
	public boolean hasKilled(String entity, int count) {
		Integer counter = killings.get(entity);
		return counter == null? false: counter >= count;
	}

	public void readFromNBT(NBTTagCompound nbt) {
		
	}
	
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		return nbt;
	}
}
