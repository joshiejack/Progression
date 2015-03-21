package joshie.crafting.data.player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import joshie.crafting.helpers.NBTHelper;
import joshie.crafting.helpers.NBTHelper.IMapHelper;
import net.minecraft.nbt.NBTTagCompound;

public class DataKillings implements IMapHelper {
	private HashMap<String, Integer> killings = new HashMap();
	
	public boolean hasKilled(String entity, int count) {
		Integer counter = killings.get(entity);
		return counter == null? false: counter >= count;
	}
	
	public int setKilled(String entityString) {
		Integer counter = killings.get(entityString);
		if (counter == null) counter = 0;
		counter++;
		
		killings.put(entityString, counter);
		return counter;
	}

	@Override
	public Map getMap() {
		return killings;
	}

	@Override
	public Object readKey(NBTTagCompound tag) {
		return tag.getString("Key");
	}

	@Override
	public Object readValue(NBTTagCompound tag) {
		return tag.getInteger("Value");
	}

	@Override
	public void writeKey(NBTTagCompound tag, Object o) {
		tag.setString("Key", (String) o);
	}

	@Override
	public void writeValue(NBTTagCompound tag, Object o) {
		tag.setInteger("Value", (Integer) o);
	}

	public void readFromNBT(NBTTagCompound nbt) {
		NBTHelper.readMap(nbt, "KillingsData", this);
	}
	
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		return NBTHelper.writeMap(nbt, "KillingsData", this);
	}
}
