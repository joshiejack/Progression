package joshie.crafting.implementation.crafters;

import java.util.UUID;

import joshie.crafting.api.CraftingAPI;
import joshie.crafting.api.ICondition;
import joshie.crafting.api.crafting.CraftingType;
import joshie.crafting.api.crafting.ICrafter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class CrafterHuman implements ICrafter {
	//List of technologies this human has unlocked
	private final UUID uuid;
	private EntityPlayer player;
	
	public CrafterHuman(UUID uuid) {
		this.uuid = uuid;
	}

	@Override
	public boolean canUseItemForCrafting(ItemStack stack) {
		return true;
	}

	@Override
	public boolean canCraftItem(CraftingType type, ItemStack stack) {
		for (ICondition condition: CraftingAPI.conditions.getConditions(stack)) {
			if (!condition.isCorrectCraftingType(type)) return false;
			if (!condition.isMet(uuid)) return false;
		}
		
		return true;
	}

	@Override
	public boolean canCraftWithAnything() {
		return true;
	}

	@Override
	public boolean canRepairItem(ItemStack stack) {
		return true;
	}
}
