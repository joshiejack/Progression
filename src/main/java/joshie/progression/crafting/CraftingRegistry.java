package joshie.progression.crafting;

import com.google.common.collect.HashMultimap;
import joshie.progression.api.criteria.ICriteria;
import joshie.progression.api.criteria.IFilter;
import joshie.progression.helpers.ItemHelper;
import joshie.progression.helpers.PlayerHelper;
import joshie.progression.player.PlayerTracker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import java.util.*;

public class CraftingRegistry {
    private static volatile HashMap<ActionType, HashMultimap<Item, IFilter>> itemToFiltersMapCrafting;
    private static volatile HashMap<ActionType, HashMap<IFilter, ICriteria>> filterToCriteriaMapCrafting;

    public static void create() {
        itemToFiltersMapCrafting = new HashMap();
        filterToCriteriaMapCrafting = new HashMap();
    }

    private static HashMultimap<Item, IFilter> getItemToFiltersForType(ActionType type) {
        HashMultimap<Item, IFilter> itemToFilters = itemToFiltersMapCrafting.get(type);
        if (itemToFilters == null) {
            itemToFilters = HashMultimap.create();
            itemToFiltersMapCrafting.put(type, itemToFilters);
        }

        return itemToFilters;
    }

    private static HashMap<IFilter, ICriteria> getFilterToCriteriaForType(ActionType type) {
        HashMap<IFilter, ICriteria> filterToCriteria = filterToCriteriaMapCrafting.get(type);
        if (filterToCriteria == null) {
            filterToCriteria = new HashMap();
            filterToCriteriaMapCrafting.put(type, filterToCriteria);
        }

        return filterToCriteria;
    }

    public static void addRequirement(ActionType type, ICriteria requirement, List<IFilter> filters) {
        HashMultimap<Item, IFilter> itemToFilters = getItemToFiltersForType(type);
        HashMap<IFilter, ICriteria> filterToCriteria = getFilterToCriteriaForType(type);

        List<ItemStack> stacks = ItemHelper.getAllMatchingItems(filters);

        //Add a link for item to filters
        for (ItemStack stack : stacks) {
            itemToFilters.get(stack.getItem()).addAll(filters); //Map all the requirements
        }

        //Add a link for filter to criteria
        for (IFilter filter : filters) {
            filterToCriteria.put(filter, requirement);
        }
    }

    public static void removeRequirement(ActionType type, ICriteria requirement, List<IFilter> filters) {
        HashMultimap<Item, IFilter> itemToFilters = getItemToFiltersForType(type);
        HashMap<IFilter, ICriteria> filterToCriteria = getFilterToCriteriaForType(type);

        for (Item key : itemToFilters.keySet()) {
            itemToFilters.get(key).removeAll(filters); //Remove any matching filters
        }

        //Remove all filters from filter to criteria
        for (IFilter filter : filters) {
            filterToCriteria.remove(filter);
        }
    }

    public static Set<IFilter> getFiltersForStack(ActionType type, ItemStack stack) {
        HashMultimap<Item, IFilter> itemToFilters = getItemToFiltersForType(type);
        Set<IFilter> result = itemToFilters.get(stack.getItem());
        return result != null ? result : new HashSet();
    }

    public static ICriteria getCriteriaForFilter(ActionType type, IFilter filter) {
        HashMap<IFilter, ICriteria> filterToCriteria = getFilterToCriteriaForType(type);
        return filterToCriteria.get(filter);
    }

    private static HashMap<ActionType, HashMap<ItemStack, Set>> actionCache = new HashMap();

    public static Set<ICriteria> getRequirements(ActionType type, ItemStack stack) {
        HashMap<ItemStack, Set> cache = actionCache.get(type);
        if (cache == null) {
            cache = new HashMap();
            actionCache.put(type, cache);
        }

        if (cache.containsKey(stack)) return cache.get(stack);
        Set<IFilter> filters = CraftingRegistry.getFiltersForStack(type, stack);
        Set<ICriteria> matched = new HashSet();
        for (IFilter filter : filters) {
            if (filter.matches(stack)) {
                matched.add(getCriteriaForFilter(type, filter)); //Add all matches so we can check all criteria
            }
        }

        cache.put(stack, matched);
        return matched;
    }

    public static Crafter getCrafterFromPlayer(EntityPlayer player) {
        return getCrafterFromUUID(PlayerHelper.getUUIDForPlayer(player));
    }

    public static Crafter getCrafterFromTile(TileEntity tile) {
        return getCrafterFromUUID(PlayerTracker.getTileOwner(tile));
    }

    private static Crafter getCrafterFromUUID(UUID uuid) {
        return PlayerHelper.getCrafterForUUID(uuid);
    }
}
