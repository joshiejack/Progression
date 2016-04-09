package joshie.progression.handlers;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import joshie.progression.api.criteria.ICriteria;
import joshie.progression.api.criteria.IRewardProvider;
import joshie.progression.api.criteria.ITab;
import joshie.progression.api.criteria.ITriggerProvider;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class APICache {
    private final Cache<Boolean, ArrayList<ITab>> sortedCache = CacheBuilder.newBuilder().maximumSize(1).expireAfterWrite(1, TimeUnit.MINUTES).build();
    private final Cache<UUID, ITriggerProvider> triggerCache = CacheBuilder.newBuilder().maximumSize(8096).build();
    private final Cache<UUID, IRewardProvider> rewardCache = CacheBuilder.newBuilder().maximumSize(8096).build();
    private final HashMap<UUID, ICriteria> criteriaCache = new HashMap();
    private final HashMap<UUID, ITab> tabCache = new HashMap();

    public IRewardProvider getRewardFromUUID(final UUID uuid) {
        try {
            return rewardCache.get(uuid, new Callable<IRewardProvider>() {
                @Override
                public IRewardProvider call() throws Exception {
                    for (ICriteria criteria: criteriaCache.values()) {
                        for (IRewardProvider reward: criteria.getRewards()) {
                            if (reward.getUniqueID().equals(uuid)) return reward;
                        }
                    }

                    return null;
                }
            });
        } catch (Exception e) {
            return null;
        }
    }

    public ITriggerProvider getTriggerFromUUID(final UUID uuid) {
        try {
            return triggerCache.get(uuid, new Callable<ITriggerProvider>() {
                @Override
                public ITriggerProvider call() throws Exception {
                    for (ICriteria criteria: getCriteria().values()) {
                        for (ITriggerProvider trigger: criteria.getTriggers()) {
                            if (trigger.getUniqueID().equals(uuid)) return trigger;
                        }
                    }

                    return null;
                }
            });
        } catch (Exception e) {
            return null;
        }
    }

    public HashMap<UUID,ICriteria> getCriteria() {
        return criteriaCache;
    }

    public HashMap<UUID,ITab> getTabs() {
        return tabCache;
    }

    public void clearSorted() {
        sortedCache.cleanUp();
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

    private static class SortIndex implements Comparator {
        @Override
        public int compare(Object o1, Object o2) {
            ITab tab1 = ((ITab) o1);
            ITab tab2 = ((ITab) o2);
            if (tab1.getSortIndex() == tab2.getSortIndex()) {
                return tab1.getDisplayName().compareTo(tab2.getDisplayName());
            }

            return tab1.getSortIndex() < tab2.getSortIndex() ? 1 : -1;
        }
    }
}
