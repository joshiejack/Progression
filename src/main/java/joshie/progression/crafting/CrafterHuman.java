package joshie.progression.crafting;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import joshie.progression.api.criteria.IProgressionCriteria;
import joshie.progression.api.criteria.IProgressionFilter;
import joshie.progression.crafting.CraftingRegistry.DisallowType;
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
        Set<IProgressionFilter> filters = CraftingRegistry.getFiltersForStack(type, stack, DisallowType.USEINCRAFTING);
        List<IProgressionFilter> matched = new ArrayList();
        for (IProgressionFilter filter : filters) {
            if (filter.matches(stack)) {
                matched.add(filter); //Add all matches so we can check all criteria
            }
        }

        if (matched.size() == 0) return !Options.settings.disableUsageUntilRewardAdded;
        Set<IProgressionCriteria> completed = PlayerTracker.getPlayerData(uuid).getMappings().getCompletedCriteria().keySet();
        for (IProgressionFilter filter : matched) {
            IProgressionCriteria criteria = CraftingRegistry.getCriteriaForFilter(type, filter, DisallowType.USEINCRAFTING);
            if (criteria != null && completed.contains(criteria)) return true;
        }
        
        return false;
    }

    @Override
    public boolean canCraftItem(ActionType type, ItemStack stack) {
        Set<IProgressionFilter> filters = CraftingRegistry.getFiltersForStack(type, stack, DisallowType.CRAFTING);
               
        List<IProgressionFilter> matched = new ArrayList();
        for (IProgressionFilter filter : filters) {
            if (filter.matches(stack)) {
                matched.add(filter); //Add all matches so we can check all criteria
            }
        }

        if (matched.size() == 0) return !Options.settings.disableCraftingUntilRewardAdded;
        Set<IProgressionCriteria> completed = PlayerTracker.getPlayerData(uuid).getMappings().getCompletedCriteria().keySet();
        for (IProgressionFilter filter : matched) {
            IProgressionCriteria criteria = CraftingRegistry.getCriteriaForFilter(type, filter, DisallowType.CRAFTING);
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
