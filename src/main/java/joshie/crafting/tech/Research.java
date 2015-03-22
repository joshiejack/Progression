package joshie.crafting.tech;

import java.util.UUID;

import joshie.crafting.api.CraftingAPI;
import joshie.crafting.api.IResearch;

public class Research implements IResearch {
	private String name;
	
	public Research(String name) {
		this.name = name;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean complete(UUID uuid) {
		return CraftingAPI.players.getPlayerData(uuid).unlock(this);
	}
}
