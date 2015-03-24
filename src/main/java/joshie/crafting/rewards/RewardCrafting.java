package joshie.crafting.rewards;

import java.util.UUID;

import joshie.crafting.api.CraftingAPI;
import joshie.crafting.api.ICriteria;
import joshie.crafting.api.IReward;
import joshie.crafting.api.crafting.CraftingType;
import joshie.crafting.helpers.StackHelper;
import net.minecraft.item.ItemStack;

import com.google.gson.JsonObject;

public class RewardCrafting extends RewardBase {
	private ItemStack stack;
	private CraftingType type = CraftingType.CRAFTING;
	private boolean matchDamage = true;
	private boolean matchNBT = false;
	
	public RewardCrafting() {
		super("crafting");
	}
	
	@Override
	public IReward deserialize(JsonObject data) {
		RewardCrafting reward = new RewardCrafting();
		reward.stack = StackHelper.getStackFromString(data.get("Item").getAsString());
		if (data.get("Crafting Type") != null) {
			String craftingtype = data.get("Crafting Type").getAsString();
			for (CraftingType type: CraftingType.values()) {
				if (type.name().equalsIgnoreCase(craftingtype)) {
					reward.type = type;
					break;
				}
			}
		}
		
		if (data.get("Match Damage") != null) {
			reward.matchDamage = data.get("MatchDamage").getAsBoolean();
		}
		
		if (data.get("Match NBT") != null) {
			reward.matchNBT = data.get("Match NBT").getAsBoolean();
		}
		
		return reward;
	}

	@Override
	public void serialize(JsonObject elements) {
		elements.addProperty("Item", StackHelper.getStringFromStack(stack));
		if (type != CraftingType.CRAFTING) {
			elements.addProperty("Crafting Type", type.name().toLowerCase());
		}
		
		if (matchDamage != true) {
			elements.addProperty("Match Damage", matchDamage);
		}
		
		if (matchNBT != false) {
			elements.addProperty("Match NBT", matchNBT);
		}
	}
	
	@Override
	public void reward(UUID uuid) {}
	
	@Override
	public void onAdded(ICriteria criteria) {
		CraftingAPI.crafting.addRequirement(type, stack, matchDamage, matchNBT, criteria);
	}
}
