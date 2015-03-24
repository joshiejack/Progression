package joshie.crafting.crafting;

import java.util.Collection;
import java.util.EnumMap;
import java.util.HashSet;

import joshie.crafting.api.ICraftingRegistry;
import joshie.crafting.api.ICriteria;
import joshie.crafting.api.crafting.CraftingType;
import joshie.crafting.lib.SafeStack;
import net.minecraft.item.ItemStack;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public class CraftingRegistry implements ICraftingRegistry {
	public EnumMap<CraftingType, Multimap<SafeStack, ICriteria>> conditions = new EnumMap(CraftingType.class);
	
	@Override
	public ICraftingRegistry init() {
		for (CraftingType type: CraftingType.values()) {
			Multimap<SafeStack, ICriteria> conditions = HashMultimap.create();
			this.conditions.put(type, conditions);
		}
		
		return this;
	}

	@Override
	public Collection<ICriteria> getConditions(CraftingType type, ItemStack stack) {
		Collection<ICriteria> conditions = new HashSet();
		SafeStack[] safe = SafeStack.allInstances(stack);
		for (SafeStack s: safe) {
			conditions.addAll(this.conditions.get(type).get(s));
		}
		
		return conditions;
	}
	
	@Override
	public void addRequirement(CraftingType type, ItemStack stack, boolean matchDamage, boolean matchNBT, ICriteria c) {
		Multimap<SafeStack, ICriteria> conditions = this.conditions.get(type);
		conditions.put(SafeStack.newInstance(stack, matchDamage, matchNBT), c);
	}

	@Override
	public void addRequirement(CraftingType type, ItemStack stack, ICriteria c) {
		addRequirement(type, stack, true, false, c);
	}
}
