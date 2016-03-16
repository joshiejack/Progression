package joshie.progression.crafting;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import joshie.progression.api.ICriteria;
import joshie.progression.api.IItemFilter;
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
        Set<IItemFilter> filters = CraftingRegistry.getFiltersForStack(type, stack, true);
        List<IItemFilter> matched = new ArrayList();
        for (IItemFilter filter : filters) {
            if (filter.matches(stack)) {
                matched.add(filter); //Add all matches so we can check all criteria
            }
        }

        if (matched.size() == 0) return !Options.settings.disableUsageUntilRewardAdded;
        Set<ICriteria> completed = PlayerTracker.getPlayerData(uuid).getMappings().getCompletedCriteria().keySet();
        for (IItemFilter filter : matched) {
            ICriteria criteria = CraftingRegistry.getCriteriaForFilter(type, filter, true);
            if (criteria != null && completed.contains(criteria)) return true;
        }
        
        return false;
    }

    @Override
    public boolean canCraftItem(ActionType type, ItemStack stack) {
        Set<IItemFilter> filters = CraftingRegistry.getFiltersForStack(type, stack, false);
        
        List<IItemFilter> matched = new ArrayList();
        for (IItemFilter filter : filters) {
            if (filter.matches(stack)) {
                matched.add(filter); //Add all matches so we can check all criteria
            }
        }

        if (matched.size() == 0) return !Options.settings.disableCraftingUntilRewardAdded;
        Set<ICriteria> completed = PlayerTracker.getPlayerData(uuid).getMappings().getCompletedCriteria().keySet();
        for (IItemFilter filter : matched) {
            ICriteria criteria = CraftingRegistry.getCriteriaForFilter(type, filter, false);
            if (criteria != null && completed.contains(criteria)) return true;
        }
        
        return false;
    }

    @Override
    public boolean canCraftWithAnything() {
        return false;
    }

    @Override
    public boolean canCraftAnything() {
        return false;
    }
}
