package joshie.crafting.implementation.conditions;

import java.util.HashMap;
import java.util.List;

import joshie.crafting.api.CraftingAPI;
import joshie.crafting.api.ICondition;
import joshie.crafting.api.IConditionHandler;
import joshie.crafting.lib.SafeStack;
import net.minecraft.item.ItemStack;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class Conditions implements IConditionHandler {
	private HashMap<String, ICondition> registry = new HashMap();
	private Multimap<SafeStack, ICondition> conditions = ArrayListMultimap.create();

	@Override
	public void registerCondition(ICondition condition) {
		registry.put(condition.getName(), condition);
	}

	@Override
	public List<ICondition> getConditions(ItemStack stack) {
		return (List<ICondition>) conditions.get(SafeStack.newInstance(stack));
	}

	@Override
	public void addCondition(ItemStack stack, ICondition... conditions) {
		SafeStack safe = SafeStack.newInstance(stack);
		for (ICondition condition: conditions) {
			this.conditions.put(safe, condition);
			condition.onAdded(stack);
		}
	}

	@Override
	public ICondition getConditionFromName(String condition) {
		return registry.get(condition);
	}
}
