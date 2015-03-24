package joshie.crafting.rewards;

import java.util.UUID;

import joshie.crafting.api.CraftingAPI;
import joshie.crafting.api.IReward;
import joshie.crafting.minetweaker.Rewards;
import minetweaker.MineTweakerAPI;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import com.google.gson.JsonObject;

@ZenClass("mods.craftcontrol.rewards.Speed")
public class RewardSpeed extends RewardBase {
	private float speed;
	
	public RewardSpeed() {
		super("speed");
	}
	
	@ZenMethod
	public void add(String unique, double speed) {
		RewardSpeed reward = new RewardSpeed();
		reward.speed = Float.parseFloat("" + speed);		
		MineTweakerAPI.apply(new Rewards(unique, reward));
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
