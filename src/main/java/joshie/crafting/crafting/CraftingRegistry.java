package joshie.crafting.crafting;

import java.util.Collection;
import java.util.EnumMap;
import java.util.HashSet;

import joshie.crafting.api.CraftingAPI;
import joshie.crafting.api.ICondition;
import joshie.crafting.api.ICraftingRegistry;
import joshie.crafting.api.crafting.CraftingType;
import joshie.crafting.lib.SafeStack;
import net.minecraft.item.ItemStack;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public class CraftingRegistry implements ICraftingRegistry {
	public EnumMap<CraftingType, Multimap<SafeStack, ICondition>> conditions = new EnumMap(CraftingType.class);
	
	@Override
	public ICraftingRegistry init() {
		for (CraftingType type: CraftingType.values()) {
			Multimap<SafeStack, ICondition> conditions = HashMultimap.create();
			this.conditions.put(type, conditions);
		}
		
		return this;
	}

	@Override
	public Collection<ICondition> getConditions(CraftingType type, ItemStack stack) {
		Collection<ICondition> conditions = new HashSet();
		SafeStack[] safe = SafeStack.allInstances(stack);
		for (SafeStack s: safe) {
			conditions.addAll(this.conditions.get(type).get(s));
		}
		
		return conditions;
	}
	
	@Override
	public void addCondition(CraftingType type, ItemStack stack, boolean matchDamage, boolean matchNBT, String... condition) {
		Multimap<SafeStack, ICondition> conditions = this.conditions.get(type);
		for (String c: condition) {
			conditions.put(SafeStack.newInstance(stack, matchDamage, matchNBT), CraftingAPI.registry.getConditionFromName(c));
		}
	}

	@Override
	public void addCondition(CraftingType type, ItemStack stack, String... condition) {
		addCondition(type, stack, true, false, condition);
	}
}
