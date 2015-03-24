package joshie.crafting.rewards;

import java.util.UUID;

import joshie.crafting.api.CraftingAPI;
import joshie.crafting.api.IReward;

import com.google.gson.JsonObject;

public class RewardSpeed extends RewardBase {
	private float speed;
	
	public RewardSpeed() {
		super("speed");
	}
	
	@Override
	public IReward deserialize(JsonObject data) {
		RewardSpeed reward = new RewardSpeed();
		reward.speed = data.get("Speed").getAsFloat();
		return reward;
	}

	@Override
	public void serialize(JsonObject elements) {
		elements.addProperty("Speed", speed);
	}
	
	@Override
	public void reward(UUID uuid) {
		CraftingAPI.players.getServerPlayer(uuid).addSpeed(speed);
	}
}
