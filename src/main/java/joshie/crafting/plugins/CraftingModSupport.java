package joshie.crafting.plugins;

import joshie.crafting.CraftAPIRegistry;
import joshie.crafting.CraftingCriteria;
import joshie.crafting.api.IConditionType;
import joshie.crafting.api.IRewardType;
import joshie.crafting.api.ITriggerType;
import minetweaker.MineTweakerAPI;

public class CraftingModSupport {
	public static void loadMineTweaker3() {
		for (ITriggerType type: CraftAPIRegistry.triggerTypes.values()) {
			MineTweakerAPI.registerClass(type.getClass());
		}
		
		for (IRewardType type: CraftAPIRegistry.rewardTypes.values()) {
			MineTweakerAPI.registerClass(type.getClass());
		}
		
		for (IConditionType type: CraftAPIRegistry.conditionTypes.values()) {
			MineTweakerAPI.registerClass(type.getClass());
		}
		
		MineTweakerAPI.registerClass(CraftingCriteria.class);
	}
}
