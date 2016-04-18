package joshie.progression.handlers;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import joshie.progression.api.criteria.*;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class APICache {
    private final Cache<Boolean, ArrayList<ITab>> sortedCache = CacheBuilder.newBuilder().maximumSize(1).expireAfterWrite(1, TimeUnit.MINUTES).build();
    private final HashMap<UUID, IRewardProvider> rewardCache = new HashMap();
    private final HashMap<UUID, ITriggerProvider> triggerCache = new HashMap();
    private final HashMap<UUID, ICriteria> criteriaCache = new HashMap();
    private final HashMap<UUID, ITab> tabCache = new HashMap();

    public IRewardProvider getRewardFromUUID(final UUID uuid) {
        return rewardCache.get(uuid);
    }

    public ITriggerProvider getTriggerFromUUID(final UUID uuid) {
        return triggerCache.get(uuid);
    }

    public void addReward(IRewardProvider reward) {
        rewardCache.put(reward.getUniqueID(), reward);
    }

    public void addTrigger(ITriggerProvider trigger) {
        triggerCache.put(trigger.getUniqueID(), trigger);
    }

    public void addCriteria(ICriteria criteria) {
        criteriaCache.put(criteria.getUniqueID(), criteria);
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
