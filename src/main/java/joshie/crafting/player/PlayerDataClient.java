package joshie.crafting.player;

import java.util.HashSet;
import java.util.UUID;

import joshie.crafting.api.ICondition;
import joshie.crafting.api.IPlayerDataClient;
import joshie.crafting.api.IResearch;
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
	public void setResearch(IResearch... researches) {
		unlocked = new HashSet();
		super.unlock(researches);
	}

	@Override
	public void setCompleted(ICondition... conditions) {
		completed = new HashSet();
		super.markCompleted(conditions);
	}

	@Override
	public UUID getUUID() {
		return PlayerHelper.getUUIDForPlayer(ClientHelper.getPlayer());
	}

	@Override
	public void setSpeed(float speed) {
		this.abilities.setSpeed(speed);
	}
	
	@Override
	public void addKill(String entity) {}

	public void setKillings(String entity, int killings) {
		kills.put(entity, killings);
	}
}
