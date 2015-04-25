package joshie.crafting.player;

import java.util.UUID;

import joshie.crafting.helpers.ClientHelper;
import joshie.crafting.helpers.PlayerHelper;


public class PlayerDataClient extends PlayerDataCommon {
	private static PlayerDataClient INSTANCE = new PlayerDataClient();
	
	public static PlayerDataClient getInstance() {
		return INSTANCE;
	}
	
	public static void newInstance() {
		INSTANCE = new PlayerDataClient();
	}

	public UUID getUUID() {
		return PlayerHelper.getUUIDForPlayer(ClientHelper.getPlayer());
	}

	public void setAbilities(DataStats abilities) {
		this.abilities = abilities;
	}
}
