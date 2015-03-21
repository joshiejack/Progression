package joshie.crafting.implementation.conditions;

import joshie.crafting.api.ICondition;
import joshie.crafting.api.crafting.CraftingType;

public abstract class ConditionBase implements ICondition {
	private CraftingType type;
	
	public boolean isCorrectCraftingType(CraftingType type) {
		return type == this.type;
	}
	
	public CraftingType getTypeFromName(String type) {
		for (CraftingType c: CraftingType.values()) {
			if (c.name().equalsIgnoreCase(type)) return c;
		}
		
		return CraftingType.CRAFTING;
	}
	
	@Override
	public ICondition setCraftingType(String type) {
		this.type = getTypeFromName(type);
		return this;
	}
}
