package joshie.crafting.player;

import java.util.HashMap;
import java.util.UUID;

import joshie.crafting.CraftingMappings;
import joshie.crafting.api.IPlayerDataClient;
import joshie.crafting.helpers.ClientHelper;
import joshie.crafting.helpers.PlayerHelper;


public class PlayerDataClient extends PlayerDataCommon implements IPlayerDataClient {
	private static PlayerDataClient INSTANCE = new PlayerDataClient();
	
	public static PlayerDataClient getInstance() {
		return INSTANCE;
	}
	
	public static void newInstance() {
		INSTANCE = new PlayerDataClient();
	}

	@Override
	public UUID getUUID() {
		return PlayerHelper.getUUIDForPlayer(ClientHelper.getPlayer());
	}

	@Override
	public void setAbilities(DataStats abilities) {
		this.abilities = abilities;
	}

	@Override
	public void resetData() {
		abilities = new DataStats();
		mappings = new CraftingMappings();
		crafts = new HashMap();
	}
}
