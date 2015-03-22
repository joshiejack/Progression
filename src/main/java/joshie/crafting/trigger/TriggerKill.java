package joshie.crafting.trigger;

import java.util.UUID;

import joshie.crafting.api.CraftingAPI;
import joshie.crafting.api.ITrigger;

public class TriggerKill implements ITrigger {
	private String entity;
	private int amount;
	
	@Override
	public ITrigger newInstance(String data) {
		String[] split = data.split("\\|");
		TriggerKill trigger = new TriggerKill();
		trigger.entity = split[0];
		trigger.amount = Integer.parseInt(split[1]);
		return null;
	}
	
	@Override
	public String getName() {
		return "kill";
	}
	
	@Override
	public boolean isSatisfied(UUID uuid) {
		return CraftingAPI.players.getPlayerData(uuid).getKillCount(entity) >= amount;
	}
}
