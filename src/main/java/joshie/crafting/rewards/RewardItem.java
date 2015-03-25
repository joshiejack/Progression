package joshie.crafting.rewards;

import java.util.UUID;

import joshie.crafting.api.IReward;
import joshie.crafting.helpers.PlayerHelper;
import joshie.crafting.helpers.SpawnItemHelper;
import joshie.crafting.helpers.StackHelper;
import joshie.crafting.plugins.minetweaker.MTHelper;
import joshie.crafting.plugins.minetweaker.Rewards;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import com.google.gson.JsonObject;

@ZenClass("mods.craftcontrol.rewards.Item")
public class RewardItem extends RewardBase {
	private ItemStack stack;
	
	public RewardItem() {
		super("Item");
	}
	
	@ZenMethod
	public void add(String unique, IItemStack stack) {
		RewardItem reward = new RewardItem();
		reward.stack = MTHelper.getItemStack(stack);			
		MineTweakerAPI.apply(new Rewards(unique, reward));
	}
	
	@Override
	public IReward deserialize(JsonObject data) {
		RewardItem reward = new RewardItem();
		reward.stack = StackHelper.getStackFromString(data.get("Item").getAsString());		
		return reward;
	}

	@Override
	public void serialize(JsonObject elements) {
		elements.addProperty("Item", StackHelper.getStringFromStack(stack));
	}
	
	@Override
	public void reward(UUID uuid) {
		EntityPlayer player = PlayerHelper.getPlayerFromUUID(uuid);
		if (player != null) {
			SpawnItemHelper.addToPlayerInventory(player, stack);
		}
	}
}
