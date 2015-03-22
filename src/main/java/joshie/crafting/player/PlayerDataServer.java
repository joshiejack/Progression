package joshie.crafting.player;

import java.util.UUID;

import joshie.crafting.CraftingCommon;
import joshie.crafting.api.CraftingAPI;
import joshie.crafting.api.ICondition;
import joshie.crafting.api.IPlayerDataServer;
import joshie.crafting.api.IResearch;
import joshie.crafting.helpers.NBTHelper;
import joshie.crafting.network.PacketHandler;
import joshie.crafting.network.PacketSyncConditions;
import joshie.crafting.network.PacketSyncResearch;
import joshie.crafting.network.PacketSyncSpeed;
import joshie.crafting.player.nbt.ConditionNBT;
import joshie.crafting.player.nbt.KillingsNBT;
import joshie.crafting.player.nbt.ResearchNBT;
import net.minecraft.nbt.NBTTagCompound;

public class PlayerDataServer extends PlayerDataCommon implements IPlayerDataServer {
	private final UUID uuid;
	
	public PlayerDataServer(UUID uuid) {
		this.uuid = uuid;
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

	@Override
	public void addKill(String entity) {
		int newKills = kills.get(entity) + 1;
		kills.put(entity, newKills);
		CraftingAPI.registry.fireTrigger(uuid, CraftingCommon.triggerKill.getName());
		markDirty();
	}
	
	@Override
	public boolean unlock(IResearch... researchs) {
		boolean updated = false;
		for (IResearch research: researchs) {
			if (unlocked.add(research)) {
				PacketHandler.sendToClient(new PacketSyncResearch(false, research), uuid);
				updated = true;
			}
		}
				
		if(updated) { //Fire the trigger once
			CraftingAPI.registry.fireTrigger(uuid, CraftingCommon.triggerResearch.getName());
		}
		
		markDirty();
		return updated;
	}

	@Override
	public void markCompleted(ICondition... conditions) {
		super.markCompleted(conditions);
		PacketHandler.sendToClient(new PacketSyncConditions(false, conditions), uuid);
		markDirty();
	}
	
	public void readFromNBT(NBTTagCompound tag) {
		abilities.readFromNBT(tag.getCompoundTag("Abilities"));
		NBTHelper.readMap(tag, "Killings", KillingsNBT.INSTANCE.setMap(kills));
		NBTHelper.readStringCollection(tag, "Research", ResearchNBT.INSTANCE.setCollection(unlocked));
		NBTHelper.readStringCollection(tag, "Conditions", ConditionNBT.INSTANCE.setCollection(completed));
	}

	public void writeToNBT(NBTTagCompound tag) {
		tag.setTag("Abilities", abilities.writeToNBT(new NBTTagCompound()));
		NBTHelper.writeMap(tag, "Killings", KillingsNBT.INSTANCE.setMap(kills));
		NBTHelper.writeCollection(tag, "Research", ResearchNBT.INSTANCE.setCollection(unlocked));
		NBTHelper.writeCollection(tag, "Conditions", ConditionNBT.INSTANCE.setCollection(completed));
	}
}
