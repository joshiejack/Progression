package joshie.crafting.player;

import java.util.UUID;

import joshie.crafting.api.CraftingAPI;
import joshie.crafting.network.PacketHandler;
import joshie.crafting.network.PacketSyncAbilities;
import net.minecraft.nbt.NBTTagCompound;

public class PlayerDataServer extends PlayerDataCommon {
	private final UUID uuid;
	
	public PlayerDataServer(UUID uuid) {
		this.uuid = uuid;
		this.mappings.setMaster(this);
	}
	
	public UUID getUUID() {
		return uuid;
	}

	public void addSpeed(float speed) {
		float newStat = abilities.getSpeed() + speed;
		abilities.setSpeed(newStat);
		PacketHandler.sendToClient(new PacketSyncAbilities(abilities), uuid);
		markDirty();
	}

	public void addFallDamagePrevention(int maxAbsorbed) {
		int newStat = abilities.getFallDamagePrevention() + maxAbsorbed;
		abilities.setFallDamagePrevention(newStat);
		PacketHandler.sendToClient(new PacketSyncAbilities(abilities), uuid);
		markDirty();
	}
	
    public void addPoints(String name, int amount) {
        int newStat = abilities.getPoints(name) + amount;
        abilities.setResearchPoints(name, newStat);
        PacketHandler.sendToClient(new PacketSyncAbilities(abilities), uuid);
        CraftingAPI.registry.fireTrigger(uuid, "points", name, newStat);
        markDirty();
    }
	
	public void readFromNBT(NBTTagCompound tag) {
		abilities.readFromNBT(tag.getCompoundTag("Abilities"));
		mappings.readFromNBT(tag.getCompoundTag("Data"));
	}

	public void writeToNBT(NBTTagCompound tag) {
		tag.setTag("Abilities", abilities.writeToNBT(new NBTTagCompound()));
		tag.setTag("Data", mappings.writeToNBT(new NBTTagCompound()));
	}
}
