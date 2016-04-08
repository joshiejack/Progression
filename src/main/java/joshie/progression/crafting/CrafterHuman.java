package joshie.progression.crafting;

import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.criteria.ICriteria;
import joshie.progression.api.criteria.IFilterProvider;
import joshie.progression.json.Options;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class CrafterHuman extends Crafter {
    //List of technologies this human has unlocked
    private final UUID uuid;
    private EntityPlayer player;

    public CrafterHuman(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public boolean canUseItemWithAction(World world, ActionType type, ItemStack stack) {
        Set<IFilterProvider> filters = CraftingRegistry.getFiltersForStack(type, stack);
        List<IFilterProvider> matched = new ArrayList();
        for (IFilterProvider filter : filters) {
            if (filter.getProvided().matches(stack)) {
                matched.add(filter); //Add all matches so we can check all criteria
            }
        }

        if (matched.size() == 0) return !Options.settings.disableUsageUntilRewardAdded;
        Set<ICriteria> completed = ProgressionAPI.player.getCompletedCriteriaList(uuid, world.isRemote);
        for (IFilterProvider filter : matched) {
            ICriteria criteria = CraftingRegistry.getCriteriaForFilter(type, filter);
            if (criteria != null && completed.contains(criteria)) return true;
        }
        
        return false;
    }

    @Override
    public boolean canDoAnything() {
        return false;
    }
}
