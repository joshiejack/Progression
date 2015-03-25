package joshie.crafting.rewards;

import java.util.UUID;

import joshie.crafting.api.IReward;
import joshie.crafting.helpers.PlayerHelper;
import joshie.crafting.plugins.minetweaker.Rewards;
import minetweaker.MineTweakerAPI;
import net.minecraft.entity.player.EntityPlayer;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import com.google.gson.JsonObject;

@ZenClass("mods.craftcontrol.rewards.Experience")
public class RewardExperience extends RewardBase {
	private int amount;
	
	public RewardExperience() {
		super("Experience");
	}
	
	@ZenMethod
	public void add(String unique, int amount) {
		RewardExperience reward = new RewardExperience();
		reward.amount = amount;			
		MineTweakerAPI.apply(new Rewards(unique, reward));
	}
	
	@Override
	public IReward deserialize(JsonObject data) {
		RewardExperience reward = new RewardExperience();
		reward.amount = data.get("Amount").getAsInt();
		return reward;
	}

	@Override
	public void serialize(JsonObject elements) {
		elements.addProperty("Amount", amount);
	}
	
	@Override
	public void reward(UUID uuid) {
		EntityPlayer player = PlayerHelper.getPlayerFromUUID(uuid);
		if (player != null) {
			player.addExperience(amount);
		}
	}
}
