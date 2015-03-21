package joshie.crafting.data;

import joshie.crafting.data.player.DataKillings;
import joshie.crafting.data.player.DataTechnology;
import net.minecraft.nbt.NBTTagCompound;


public class PlayerData {
	public DataKillings killings;
	public DataTechnology technology;
	
	public PlayerData() {
		killings = new DataKillings();
		technology = new DataTechnology();
	}

	public PlayerData(NBTTagCompound tag) {
		this();
		
		readFromNBT(tag);
	}

	public DataTechnology getTechnologies() {
		return technology;
	}

	public DataKillings getKillings() {
		return killings;
	}

	public void readFromNBT(NBTTagCompound data) {
		killings.readFromNBT(data.getCompoundTag("Killings"));
		technology.readFromNBT(data.getCompoundTag("Technology"));
	}
	
	public void writeToNBT(NBTTagCompound data) {
		data.setTag("Technology", killings.writeToNBT(new NBTTagCompound()));
		data.setTag("Killings", technology.writeToNBT(new NBTTagCompound()));
	}
}
