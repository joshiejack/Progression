package joshie.progression.crafting;

import joshie.progression.api.criteria.ICriteria;
import joshie.progression.api.criteria.IFilterProvider;
import joshie.progression.helpers.PlayerHelper;
import joshie.progression.player.PlayerTracker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import java.util.*;

public class CraftingRegistry {
    private static volatile HashMap<ActionType, HashMap<IFilterProvider, ICriteria>> filterToCriteriaMapCrafting;

    public static void create() {
        filterToCriteriaMapCrafting = new HashMap();
        itemToFilterCache = new HashMap();
        actionCache = new HashMap();
    }

    private static HashMap<IFilterProvider, ICriteria> getFilterToCriteriaForType(ActionType type) {
        HashMap<IFilterProvider, ICriteria> filterToCriteria = filterToCriteriaMapCrafting.get(type);
        if (filterToCriteria == null) {
            filterToCriteria = new HashMap();
            filterToCriteriaMapCrafting.put(type, filterToCriteria);
        }

        return filterToCriteria;
    }

    public static void addRequirement(ActionType type, ICriteria requirement, List<IFilterProvider> filters) {
        HashMap<IFilterProvider, ICriteria> filterToCriteria = getFilterToCriteriaForType(type);

        //Add a link for filter to criteria
        for (IFilterProvider filter : filters) {
            filterToCriteria.put(filter, requirement);
        }
    }

    private static HashMap<ActionType, HashMap<Item, Set<IFilterProvider>>> itemToFilterCache = new HashMap();

    public static Set<IFilterProvider> getFiltersForStack(ActionType type, ItemStack stack) {
        HashMap<Item, Set<IFilterProvider>> typeCache = itemToFilterCache.get(type);
        if (typeCache == null) { //Create the type cache if it doesn't exist
            typeCache = new HashMap();
            itemToFilterCache.put(type, typeCache);
        }

        Set<IFilterProvider> result = typeCache.get(stack.getItem());
        if (result != null) return result;
        else {
            result = new HashSet();
            HashMap<IFilterProvider, ICriteria> filterToCriteria = getFilterToCriteriaForType(type);
            for (IFilterProvider filter: filterToCriteria.keySet()) {
                if (filter.getProvided().matches(stack)) result.add(filter);
            }

            typeCache.put(stack.getItem(), result);
        }

        return result != null ? result : new HashSet();
    }

    public static ICriteria getCriteriaForFilter(ActionType type, IFilterProvider filter) {
        return getFilterToCriteriaForType(type).get(filter);
    }

    private static HashMap<ActionType, HashMap<ItemStack, Set<ICriteria>>> actionCache = new HashMap();

    public static Set<ICriteria> getRequirements(ActionType type, ItemStack stack) {
        Set<ICriteria> matched = new HashSet();
        Set<IFilterProvider> filters = CraftingRegistry.getFiltersForStack(type, stack);
        for (IFilterProvider filter : filters) {
            if (filter.getProvided().matches(stack)) {
                matched.add(getCriteriaForFilter(type, filter));
            }
        }

        return matched;

        /*

        HashMap<ItemStack, Set<ICriteria>> cache = actionCache.get(type);
        if (cache == null) {
            cache = new HashMap();
            actionCache.put(type, cache);
        }

        if (cache.containsKey(stack)) return cache.get(stack);
        Set<IFilterProvider> filters = CraftingRegistry.getFiltersForStack(type, stack);
        Set<ICriteria> matched = new HashSet();
        for (IFilterProvider filter : filters) {
            if (filter.getProvided().matches(stack)) {
                matched.add(getCriteriaForFilter(type, filter)); //Add all matches so we can check all criteria
            }
        }

        cache.put(stack, matched);
        return matched; */
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
