package joshie.progression.handlers;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import joshie.progression.api.criteria.*;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.Callable;

public class APICache {
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
}
