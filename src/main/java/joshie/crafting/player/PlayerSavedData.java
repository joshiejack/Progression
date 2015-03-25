package joshie.crafting.player;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

import joshie.crafting.api.IPlayerDataServer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.WorldSavedData;

import com.google.common.collect.Maps;

public class PlayerSavedData extends WorldSavedData {
	private HashMap<UUID, PlayerDataServer> data = Maps.newHashMap();
	
	public PlayerSavedData(String data) {
		super(data);
	}
	
	public Collection<PlayerDataServer> getPlayerData() {
		return data.values();
	}

	public IPlayerDataServer getServerPlayer(UUID uuid) {
		PlayerDataServer data = this.data.get(uuid);
		if (data == null) {
			data = new PlayerDataServer(uuid);
			this.data.put(uuid, data);
			this.markDirty();
		}
		
		return data;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		NBTTagList map = nbt.getTagList("PlayerData", 10);
		for (int i = 0; i < map.tagCount(); i++) {
			NBTTagCompound tag = map.getCompoundTagAt(i);
			UUID uuid = new UUID(tag.getLong("UUID-Most"), tag.getLong("UUID-Least"));
			PlayerDataServer server = new PlayerDataServer(uuid);
			server.readFromNBT(tag);
			data.put(uuid, server);
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		NBTTagList map = new NBTTagList();
		for (UUID uuid: data.keySet()) {
			if (uuid != null) {
				NBTTagCompound tag = new NBTTagCompound();
				tag.setLong("UUID-Most", uuid.getMostSignificantBits());
				tag.setLong("UUID-Least", uuid.getLeastSignificantBits());
				data.get(uuid).writeToNBT(tag);
				map.appendTag(tag);
			}
		}
		
		nbt.setTag("PlayerData", map);
	}
}
