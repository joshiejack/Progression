package joshie.crafting.data;

import java.util.HashMap;
import java.util.UUID;

import joshie.crafting.CraftingMod;
import joshie.crafting.api.tech.ITechnology;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import com.google.common.collect.Maps;

public class DataServer extends DataCommon {
	public static final DataServer INSTANCE = new DataServer();
	private HashMap<UUID, PlayerData> data = Maps.newHashMap();
	
	private PlayerData getPlayerData(UUID uuid) {
		PlayerData data = this.data.get(uuid);
		if (data == null) {
			data = new PlayerData();
			this.data.put(uuid, data);
			CraftingMod.saveData.markDirty();
		}
		
		return data;
	}

	@Override
	public boolean hasUnlocked(UUID uuid, ITechnology technology) {
		return getPlayerData(uuid).getTechnologies().hasUnlocked(technology);
	}
	
	@Override
	public boolean unlockResearch(UUID uuid, ITechnology tech) {
		boolean ret = getPlayerData(uuid).getTechnologies().unlockResearch(tech);
		//TODO: Send Packet to client to update their tech
		CraftingMod.saveData.markDirty();
		return ret;
	}
	
	@Override
	public boolean hasKilled(UUID uuid, String entity, int count) {
		return getPlayerData(uuid).getKillings().hasKilled(entity, count);
	}

	public void readFromNBT(NBTTagCompound nbt) {
		NBTTagList techMap = nbt.getTagList("PlayerData", 10);
		for (int i = 0; i < techMap.tagCount(); i++) {
			NBTTagCompound tag = techMap.getCompoundTagAt(i);
			UUID uuid = new UUID(tag.getLong("UUID-Most"), tag.getLong("UUID-Least"));
			data.put(uuid, new PlayerData(tag));
		}
	}
	
	public void writeToNBT(NBTTagCompound nbt) {
		NBTTagList techMap = new NBTTagList();
		for (UUID uuid: data.keySet()) {
			NBTTagCompound tag = new NBTTagCompound();
			tag.setLong("UUID-Most", uuid.getMostSignificantBits());
			tag.setLong("UUID-Least", uuid.getLeastSignificantBits());
			data.get(uuid).writeToNBT(tag);
		}
		
		nbt.setTag("PlayerData", techMap);
	}
}
