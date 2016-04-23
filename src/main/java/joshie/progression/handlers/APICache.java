package joshie.progression.handlers;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import joshie.progression.api.criteria.ICriteria;
import joshie.progression.api.criteria.IRewardProvider;
import joshie.progression.api.criteria.ITab;
import joshie.progression.api.criteria.ITriggerProvider;

import java.util.*;
import java.util.concurrent.Callable;

public class APICache {
    private final Cache<Boolean, ArrayList<ITab>> sortedCache = CacheBuilder.newBuilder().maximumSize(1).build();
    private final HashMap<UUID, IRewardProvider> rewardCache = new HashMap();
    private final HashMap<UUID, ITriggerProvider> triggerCache = new HashMap();
    private final HashMap<UUID, ICriteria> criteriaCache = new HashMap();
    private final HashMap<UUID, ITab> tabCache = new HashMap();

    //Instances
    public static APICache serverCache;
    public static APICache clientCache;

    public static void resetAPIHandler(boolean isClient) {
        if (isClient) {
            clientCache = new APICache();
        } else serverCache = new APICache();
    }

    public static APICache getClientCache() {
        return clientCache;
    }

    public static APICache getServerCache() {
        return serverCache;
    }

    public static APICache getCache(boolean isClient) {
        return isClient ? getClientCache() : getServerCache();
    }

    public IRewardProvider getRewardFromUUID(final UUID uuid) {
        return rewardCache.get(uuid);
    }

    public ITriggerProvider getTriggerFromUUID(final UUID uuid) {
        return triggerCache.get(uuid);
    }

    public void addReward(IRewardProvider reward) {
        rewardCache.put(reward.getUniqueID(), reward);
    }

    public ITriggerProvider addTrigger(ITriggerProvider trigger) {
        triggerCache.put(trigger.getUniqueID(), trigger);
        return trigger;
    }

    public ICriteria addCriteria(ICriteria criteria) {
        criteriaCache.put(criteria.getUniqueID(), criteria);
        return criteria;
    }

    public void removeCriteria(ICriteria criteria) {
        criteriaCache.remove(criteria.getUniqueID());
    }

    public ICriteria getCriteria(UUID uuid) {
        return criteriaCache.get(uuid);
    }

    public Collection<ICriteria> getCriteriaSet() {
        return criteriaCache.values();
    }

    public ITab getTab(UUID uuid) {
        return tabCache.get(uuid);
    }

    public Collection<ITab> getTabSet() {
        return tabCache.values();
    }

    public Set<UUID> getTabIDs() {
        return tabCache.keySet();
    }

    public void clearSorted() {
        sortedCache.invalidateAll();
    }

    public ITab addTab(ITab tab) {
        tabCache.put(tab.getUniqueID(), tab);
        clearSorted();
        return tab;
    }

    public void removeTab(ITab tab) {
        tabCache.remove(tab.getUniqueID());
        clearSorted();
    }

    public ArrayList<ITab> getSortedTabs() {
        try {
            return sortedCache.get(true, new Callable<ArrayList<ITab>>() {
                @Override
                public ArrayList<ITab> call() throws Exception {
                    ArrayList<ITab> tabs = new ArrayList(tabCache.values());
                    Collections.sort(tabs, new SortIndex());
                    return tabs;
                }
            });
        } catch (Exception e) { return  new ArrayList(); }
    }

    public ICriteria getRandomCriteria() {
        List<ICriteria> criteria = new ArrayList<ICriteria>(criteriaCache.values());
        if (criteria.size() > 0) {
            Collections.shuffle(criteria);
            return criteria.get(0);
        } else return null;
    }

    private static class SortIndex implements Comparator {
        @Override
        public int compare(Object o1, Object o2) {
            ITab tab1 = ((ITab) o1);
            ITab tab2 = ((ITab) o2);
            if (tab1.getSortIndex() == tab2.getSortIndex()) {
                return tab1.getLocalisedName().compareTo(tab2.getLocalisedName());
            }

            return tab1.getSortIndex() < tab2.getSortIndex() ? 1 : -1;
        }
    }
}
