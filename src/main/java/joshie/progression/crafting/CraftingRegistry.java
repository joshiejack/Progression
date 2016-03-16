package joshie.progression.crafting;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.google.common.collect.HashMultimap;

import joshie.progression.api.ICriteria;
import joshie.progression.api.IFilter;
import joshie.progression.helpers.ItemHelper;
import joshie.progression.helpers.PlayerHelper;
import joshie.progression.player.PlayerTracker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class CraftingRegistry {
    private static volatile HashMap<ActionType, HashMultimap<Item, IFilter>> itemToFiltersMapCrafting;
    private static volatile HashMap<ActionType, HashMultimap<Item, IFilter>> itemToFiltersMapUsage;
    private static volatile HashMap<ActionType, HashMap<IFilter, ICriteria>> filterToCriteriaMapCrafting;
    private static volatile HashMap<ActionType, HashMap<IFilter, ICriteria>> filterToCriteriaMapUsage;

    public static void create() {
        itemToFiltersMapCrafting = new HashMap();
        itemToFiltersMapUsage = new HashMap();
        filterToCriteriaMapCrafting = new HashMap();
        filterToCriteriaMapUsage = new HashMap();
    }

    private static HashMap<ActionType, HashMultimap<Item, IFilter>> getItemToFiltersMap(boolean usage) {
        return usage ? itemToFiltersMapUsage : itemToFiltersMapCrafting;
    }

    private static HashMap<ActionType, HashMap<IFilter, ICriteria>> getFilterToCriteriaMap(boolean usage) {
        return usage ? filterToCriteriaMapUsage : filterToCriteriaMapCrafting;
    }

    private static HashMultimap<Item, IFilter> getItemToFiltersForType(ActionType type, boolean usage) {
        HashMap<ActionType, HashMultimap<Item, IFilter>> itemToFiltersMap = getItemToFiltersMap(usage);
        HashMultimap<Item, IFilter> itemToFilters = itemToFiltersMap.get(type);
        if (itemToFilters == null) {
            itemToFilters = HashMultimap.create();
            itemToFiltersMap.put(type, itemToFilters);
        }

        return itemToFilters;
    }

    private static HashMap<IFilter, ICriteria> getFilterToCriteriaForType(ActionType type, boolean usage) {
        HashMap<ActionType, HashMap<IFilter, ICriteria>> filterToCriteriaMap = getFilterToCriteriaMap(usage);
        HashMap<IFilter, ICriteria> filterToCriteria = filterToCriteriaMap.get(type);
        if (filterToCriteria == null) {
            filterToCriteria = new HashMap();
            filterToCriteriaMap.put(type, filterToCriteria);
        }

        return filterToCriteria;
    }

    private static void addRequirement(ActionType type, ICriteria requirement, List<IFilter> filters, boolean usage) {
        HashMultimap<Item, IFilter> itemToFilters = getItemToFiltersForType(type, usage);
        HashMap<IFilter, ICriteria> filterToCriteria = getFilterToCriteriaForType(type, usage);

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

    public static void addRequirement(ActionType type, ICriteria requirement, List<IFilter> filters, boolean usage, boolean crafting) {
        if (usage) addRequirement(type, requirement, filters, true);
        if (crafting) addRequirement(type, requirement, filters, false);
    }

    private static void removeRequirement(ActionType type, ICriteria requirement, List<IFilter> filters, boolean usage) {
        HashMultimap<Item, IFilter> itemToFilters = getItemToFiltersForType(type, usage);
        HashMap<IFilter, ICriteria> filterToCriteria = getFilterToCriteriaForType(type, usage);

        for (Item key : itemToFilters.keySet()) {
            itemToFilters.get(key).removeAll(filters); //Remove any matching filters
        }

        //Remove all filters from filter to criteria
        for (IFilter filter : filters) {
            filterToCriteria.remove(filter);
        }
    }

    public static void removeRequirement(ActionType type, ICriteria requirement, List<IFilter> filters, boolean usage, boolean crafting) {
        if (usage) removeRequirement(type, requirement, filters, true);
        if (crafting) removeRequirement(type, requirement, filters, false);
    }

    public static Set<IFilter> getFiltersForStack(ActionType type, ItemStack stack, boolean usage) {
        HashMultimap<Item, IFilter> itemToFilters = getItemToFiltersForType(type, usage);
        Set<IFilter> result = itemToFilters.get(stack.getItem());
        return result != null ? result : new HashSet();
    }

    public static ICriteria getCriteriaForFilter(ActionType type, IFilter filter, boolean usage) {
        HashMap<IFilter, ICriteria> filterToCriteria = getFilterToCriteriaForType(type, usage);
        return filterToCriteria.get(filter);
    }

    private static HashMap<ActionType, HashMap<ItemStack, Set>> actionCache = new HashMap();

    public static Set<ICriteria> getRequirements(ItemStack stack) {
        Set<ICriteria> matched = new HashSet();
        for (ActionType type : ActionType.values()) {
            matched.addAll(getRequirements(type, stack));
        }

        return matched;
    }

    public static Set<ICriteria> getRequirements(ActionType type, ItemStack stack) {
        HashMap<ItemStack, Set> cache = actionCache.get(type);
        if (cache == null) {
            cache = new HashMap();
            actionCache.put(type, cache);
        }

        if (cache.containsKey(stack)) return cache.get(stack);
        Set<IFilter> filters = CraftingRegistry.getFiltersForStack(type, stack, false);
        Set<ICriteria> matched = new HashSet();
        for (IFilter filter : filters) {
            if (filter.matches(stack)) {
                matched.add(getCriteriaForFilter(type, filter, false)); //Add all matches so we can check all criteria
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

    public static Crafter getCrafterFromUUID(UUID uuid) {
        return PlayerHelper.getCrafterForUUID(uuid);
    }
}
