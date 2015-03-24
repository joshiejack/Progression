package joshie.crafting.rewards;

import java.util.UUID;

import joshie.crafting.CraftingCommon;
import joshie.crafting.api.CraftingAPI;
import joshie.crafting.api.ICriteria;
import joshie.crafting.api.IReward;
import joshie.crafting.api.crafting.CraftingType;
import joshie.crafting.helpers.StackHelper;
import joshie.crafting.minetweaker.Rewards;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.minecraft.MineTweakerMC;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import codechicken.nei.api.API;

import com.google.gson.JsonObject;

@ZenClass("mods.craftcontrol.rewards.Crafting")
public class RewardCrafting extends RewardBase {
	private ItemStack stack;
	private CraftingType type = CraftingType.CRAFTING;
	private boolean matchDamage = true;
	private boolean matchNBT = false;
	
	public RewardCrafting() {
		super("crafting");
	}
	
	@ZenMethod
	public void add(String unique, IIngredient stack, @Optional String type, @Optional boolean matchNBT) {
		RewardCrafting reward = new RewardCrafting();
		reward.stack = MineTweakerMC.getItemStack(stack); 
		if (reward.stack.getItemDamage() == OreDictionary.WILDCARD_VALUE) {
			reward.matchDamage = false;
		}
		
		reward.matchNBT = matchNBT;
		for (CraftingType typeS: CraftingType.values()) {
			if (typeS.name().equalsIgnoreCase(type)) {
				reward.type = typeS;
				break;
			}
		}
		
		MineTweakerAPI.apply(new Rewards(unique, reward));
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
		
		if (CraftingCommon.NEI_LOADED) {
			if (data.get("Hide from NEI") != null) {
				if (data.get("Hide from NEI").getAsBoolean() == false) {
					reward.isAdded = false;
					API.hideItem(stack);
				}
			}
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
	
	private boolean isAdded = true;
	
	@Override
	public void reward(UUID uuid) {
		if (CraftingCommon.NEI_LOADED && !isAdded) {
			API.addItemListEntry(stack);
			isAdded = true;
		}
	}
	
	@Override
	public void onAdded(ICriteria criteria) {
		CraftingAPI.crafting.addRequirement(type, stack, matchDamage, matchNBT, criteria);
	}
}
