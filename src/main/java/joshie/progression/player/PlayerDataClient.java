package joshie.progression.player;

import java.util.UUID;

import joshie.progression.helpers.ClientHelper;
import joshie.progression.helpers.PlayerHelper;


public class PlayerDataClient extends PlayerDataCommon {
	private static PlayerDataClient INSTANCE = new PlayerDataClient();
	
	public static PlayerDataClient getInstance() {
		return INSTANCE;
	}
	
	public UUID getUUID() {
		return PlayerHelper.getUUIDForPlayer(ClientHelper.getPlayer());
	}

	public void setAbilities(DataStats abilities) {
		this.abilities = abilities;
	}
}
