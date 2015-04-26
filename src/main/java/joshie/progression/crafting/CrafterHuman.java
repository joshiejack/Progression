package joshie.progression.crafting;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

import joshie.progression.api.ICriteria;
import joshie.progression.criteria.Criteria;
import joshie.progression.json.Options;
import joshie.progression.player.PlayerTracker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class CrafterHuman extends Crafter {
	//List of technologies this human has unlocked
	private final UUID uuid;
	private EntityPlayer player;
	
	public CrafterHuman(UUID uuid) {
		this.uuid = uuid;
	}

	@Override
	public boolean canUseItemForCrafting(ActionType type, ItemStack stack) {
		Collection<ICriteria> conditions = CraftingRegistry.getCraftUsageCriteria(type, stack);
		if (conditions.size() < 1) return !Options.requireUnlockForUsage;
		Set<Criteria> completed = PlayerTracker.getPlayerData(uuid).getMappings().getCompletedCriteria().keySet();
		if (completed.containsAll(conditions)) {
			return true;
		} else return false;
	}

	@Override
	public boolean canCraftItem(ActionType type, ItemStack stack) {
		Collection<ICriteria> conditions = CraftingRegistry.getCraftingCriteria(type, stack);
		if (conditions.size() < 1) return !Options.requireUnlockForCrafting;
		Set<Criteria> completed = PlayerTracker.getPlayerData(uuid).getMappings().getCompletedCriteria().keySet();
		if (completed.containsAll(conditions)) {
			return true;
		} else return false;
	}

	@Override
	public boolean canCraftWithAnything() {
		return false;
	}
}
