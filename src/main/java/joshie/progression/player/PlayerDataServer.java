package joshie.progression.player;

import java.util.UUID;

import joshie.progression.api.ProgressionAPI;
import joshie.progression.network.PacketHandler;
import joshie.progression.network.PacketSyncAbilities;
import net.minecraft.nbt.NBTTagCompound;

public class PlayerDataServer extends PlayerDataCommon {
    private final PlayerTeam team;
	private final UUID uuid;
	
	public PlayerDataServer(PlayerTeam team) {
	    this.team = team;
		this.uuid = team.getOwner();
		this.mappings.setMaster(this);
	}
	
	public PlayerTeam getTeam() {
	    return team;
	}
	
	public UUID getUUID() {
		return uuid;
	}

	public void addSpeed(float speed) {
		float newStat = abilities.getSpeed() + speed;
		abilities.setSpeed(newStat);
		PacketHandler.sendToTeam(new PacketSyncAbilities(abilities), team);
		markDirty();
	}

	public void addFallDamagePrevention(int maxAbsorbed) {
		int newStat = abilities.getFallDamagePrevention() + maxAbsorbed;
		abilities.setFallDamagePrevention(newStat);
		PacketHandler.sendToTeam(new PacketSyncAbilities(abilities), team);
		markDirty();
	}
	
    public void addPoints(String name, int amount) {
        int newStat = abilities.getPoints(name) + amount;
        abilities.setResearchPoints(name, newStat);
        PacketHandler.sendToTeam(new PacketSyncAbilities(abilities), team);
        ProgressionAPI.registry.fireTrigger(uuid, "points", name, newStat);
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
