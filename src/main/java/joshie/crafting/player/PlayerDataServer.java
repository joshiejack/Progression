package joshie.crafting.player;

import java.util.UUID;

import joshie.crafting.api.IPlayerDataServer;
import joshie.crafting.network.PacketHandler;
import joshie.crafting.network.PacketSyncSpeed;
import net.minecraft.nbt.NBTTagCompound;

public class PlayerDataServer extends PlayerDataCommon implements IPlayerDataServer {
	private final UUID uuid;
	
	public PlayerDataServer(UUID uuid) {
		this.uuid = uuid;
		this.mappings.setMaster(this);
	}
	
	@Override
	public UUID getUUID() {
		return uuid;
	}

	@Override
	public void addSpeed(float speed) {
		float newSpeed = abilities.getSpeed() + speed;
		abilities.setSpeed(newSpeed);
		PacketHandler.sendToClient(new PacketSyncSpeed(newSpeed), uuid);
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
