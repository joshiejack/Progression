package joshie.crafting.implementation.conditions;

import java.util.UUID;

import joshie.crafting.api.ICondition;
import joshie.crafting.helpers.DataHelper;
import net.minecraft.item.ItemStack;

public class ConditionKill extends ConditionBase {
	private String entity;
	private int count;

	@Override
	public ICondition newInstance(String data) {
		ConditionKill clone = new ConditionKill();
		String[] split = null;
		if (data.contains("\\ ")) {
			split = data.split(" ");
		} else split = new String[] { data, "1" };
		
		clone.entity = split[0];
		clone.count = Integer.parseInt(split[1]);
		return clone;
	}
	
	@Override
	public boolean isMet(UUID uuid) {
		return DataHelper.getData().hasKilled(uuid, entity, count);
	}

	@Override
	public void onAdded(ItemStack stack) {}

	@Override
	public String getName() {
		return "kill";
	}
}
