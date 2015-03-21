package joshie.crafting.implementation.conditions;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import joshie.crafting.api.CraftingAPI;
import joshie.crafting.api.ICondition;
import joshie.crafting.api.tech.ITechnology;
import joshie.crafting.helpers.DataHelper;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;

public class ConditionTechnology extends ConditionBase {
	private Set<ItemStack> stacks = new HashSet();
	private ITechnology technology;

	@Override
	public ICondition newInstance(String data) {
		ConditionTechnology clone = new ConditionTechnology();
		clone.technology = CraftingAPI.tech.getTechnologyFromName(data);
		return clone;
	}
	
	@Override
	public boolean isMet(UUID uuid) {
		return DataHelper.getData().hasUnlocked(uuid, technology);
	}

	@Override
	public void onAdded(ItemStack stack) {
		stacks.add(stack);
	}

	@Override
	public String getName() {
		return "technology";
	}

	public void display(GuiScreen gui) {}
}
