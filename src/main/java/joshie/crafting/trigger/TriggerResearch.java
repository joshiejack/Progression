package joshie.crafting.trigger;

import java.util.UUID;

import joshie.crafting.api.CraftingAPI;
import joshie.crafting.api.IResearch;
import joshie.crafting.api.ITrigger;
import joshie.crafting.tech.Research;

public class TriggerResearch implements ITrigger {
	private IResearch research;

	@Override
	public ITrigger newInstance(String data) {
		TriggerResearch trigger = new TriggerResearch();
		IResearch existing = CraftingAPI.registry.getResearchFromName(data);
		if (existing == null) { //If the research doesn't exist yet, create it
			existing = CraftingAPI.registry.registerResearch(new Research(data));
		}
		
		trigger.research = existing;
		return trigger;
	}
	
	@Override
	public String getName() {
		return "research";
	}

	@Override
	public boolean isSatisfied(UUID uuid) {
		return CraftingAPI.players.getPlayerData(uuid).getUnlockedResearch().contains(research);
	}
}
