package joshie.crafting.rewards;

import java.util.UUID;

import joshie.crafting.CraftingMod;
import joshie.crafting.api.CraftingAPI;
import joshie.crafting.api.ICriteria;
import joshie.crafting.api.IReward;
import joshie.crafting.api.crafting.CraftingType;
import joshie.crafting.helpers.StackHelper;
import joshie.crafting.plugins.minetweaker.MTHelper;
import joshie.crafting.plugins.minetweaker.Rewards;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
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
	private boolean usage = true;
	private boolean crafting = true;
	
	public RewardCrafting() {
		super("crafting");
	}
	
	@ZenMethod
	public void addUsage(String unique, IIngredient stack, @Optional String type, @Optional boolean matchNBT) {
		add(unique, stack, type, matchNBT, true, false);
	}
	
	@ZenMethod
	public void addCrafting(String unique, IIngredient stack, @Optional String type, @Optional boolean matchNBT) {
		add(unique, stack, type, matchNBT, false, true);
	}
	
	@ZenMethod
	public void addRequirement(String unique, IIngredient stack, @Optional String type, @Optional boolean matchNBT) {
		add(unique, stack, type, matchNBT, true, true);
	}
	
	public void add(String unique, IIngredient stack, String type, boolean matchNBT, boolean usage, boolean crafting) {
		RewardCrafting reward = new RewardCrafting();
		reward.stack = MTHelper.getItemStack(stack);
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
		
		reward.usage = usage;
		reward.crafting = crafting;
		
		MineTweakerAPI.apply(new Rewards(unique, reward));
	}
	
	@Override
	public IReward deserialize(JsonObject data) {
		RewardCrafting reward = new RewardCrafting();
		reward.stack = StackHelper.getStackFromString(data.get("item").getAsString());
		if (data.get("craftingType") != null) {
			String craftingtype = data.get("craftingType").getAsString();
			for (CraftingType type: CraftingType.values()) {
				if (type.name().equalsIgnoreCase(craftingtype)) {
					reward.type = type;
					break;
				}
			}
		}
		
		if (data.get("matchDamage") != null) {
			reward.matchDamage = data.get("matchDamage").getAsBoolean();
		}
		
		if (data.get("matchNBT") != null) {
			reward.matchNBT = data.get("matchNBT").getAsBoolean();
		}
		
		if (data.get("disableCrafting") != null) {
			reward.crafting = data.get("disableCrafting").getAsBoolean();
		}
		
		if (data.get("disableUsage") != null) {
			reward.usage = data.get("disableUsage").getAsBoolean();
		}
		
		if (CraftingMod.NEI_LOADED) {
			if (data.get("hideFromNEI") != null) {
				if (data.get("hideFromNEI").getAsBoolean() == false) {
					reward.isAdded = false;
					API.hideItem(stack);
				}
			}
		}
		
		return reward;
	}

	@Override
	public void serialize(JsonObject elements) {
		elements.addProperty("item", StackHelper.getStringFromStack(stack));
		if (type != CraftingType.CRAFTING) {
			elements.addProperty("craftingType", type.name().toLowerCase());
		}
		
		if (matchDamage != true) {
			elements.addProperty("matchDamage", matchDamage);
		}
		
		if (matchNBT != false) {
			elements.addProperty("matchNBT", matchNBT);
		}
		
		if (crafting != true) {
			elements.addProperty("disableCrafting", false);
		}
		
		if (usage != true) {
			elements.addProperty("disableUsage", false);
		}
	}
	
	private boolean isAdded = true;
	
	@Override
	public void reward(UUID uuid) {
		if (CraftingMod.NEI_LOADED && !isAdded) {
			API.addItemListEntry(stack);
			isAdded = true;
		}
	}
	
	@Override
	public void onAdded(ICriteria criteria) {
		CraftingAPI.crafting.addRequirement(type, stack, matchDamage, matchNBT, usage, crafting, criteria);
	}


	//TODO: Replace this with an item which overlay the item
	//With some of crafting representation
    @Override
    public ItemStack getIcon() {
        return stack;
    }
}
