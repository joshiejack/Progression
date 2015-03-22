package joshie.crafting.rewards;

import java.util.UUID;

import joshie.crafting.api.CraftingAPI;
import joshie.crafting.api.IReward;

public class RewardSpeed implements IReward {
	private float speed;
	
	@Override
	public IReward newInstance(String data) {
		RewardSpeed reward = new RewardSpeed();
		reward.speed = Float.parseFloat(data);
		return reward;
	}
	
	@Override
	public void reward(UUID uuid) {
		CraftingAPI.players.getServerPlayer(uuid).addSpeed(speed);
	}

	@Override
	public String getName() {
		return "speed";
	}
}
