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
    private HashMap<ActionType, HashMap<IFilterProvider, ICriteria>> filterToCriteriaMapCrafting;
    private HashMap<ActionType, HashMap<Item, Set<IFilterProvider>>> itemToFilterCache;

    //Instances
    public static CraftingRegistry client;
    public static CraftingRegistry server;

    public static void resetRegistry(boolean isClient) {
        if (isClient) {
            client = new CraftingRegistry().create();
        } else server = new CraftingRegistry().create();
    }

    public static CraftingRegistry getClientCache() {
        return client;
    }

    public static CraftingRegistry getServerCache() {
        return server;
    }

    public static CraftingRegistry get(boolean isClient) {
        return isClient ? getClientCache() : getServerCache();
    }

    public CraftingRegistry create() {
        filterToCriteriaMapCrafting = new HashMap();
        itemToFilterCache = new HashMap();
        return this;
    }

    private HashMap<IFilterProvider, ICriteria> getFilterToCriteriaForType(ActionType type) {
        HashMap<IFilterProvider, ICriteria> filterToCriteria = filterToCriteriaMapCrafting.get(type);
        if (filterToCriteria == null) {
            filterToCriteria = new HashMap();
            filterToCriteriaMapCrafting.put(type, filterToCriteria);
        }

        return filterToCriteria;
    }

    public void addRequirement(ActionType type, ICriteria requirement, List<IFilterProvider> filters) {
        HashMap<IFilterProvider, ICriteria> filterToCriteria = getFilterToCriteriaForType(type);

        //Add a link for filter to criteria
        for (IFilterProvider filter : filters) {
            filterToCriteria.put(filter, requirement);
        }
    }



    public Set<IFilterProvider> getFiltersForStack(ActionType type, ItemStack stack) {
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

    public ICriteria getCriteriaForFilter(ActionType type, IFilterProvider filter) {
        return getFilterToCriteriaForType(type).get(filter);
    }

    public Set<ICriteria> getRequirements(ActionType type, ItemStack stack) {
        Set<ICriteria> matched = new HashSet();
        Set<IFilterProvider> filters = getFiltersForStack(type, stack);
        for (IFilterProvider filter : filters) {
            if (filter.getProvided().matches(stack)) {
                matched.add(getCriteriaForFilter(type, filter));
            }
        }

        return matched;
    }

    public Crafter getCrafterFromPlayer(EntityPlayer player) {
        return getCrafterFromUUID(PlayerHelper.getUUIDForPlayer(player));
    }

    public Crafter getCrafterFromTile(TileEntity tile) {
        return getCrafterFromUUID(PlayerTracker.getTileOwner(tile));
    }

    private Crafter getCrafterFromUUID(UUID uuid) {
        return PlayerHelper.getCrafterForUUID(uuid);
    }
}
