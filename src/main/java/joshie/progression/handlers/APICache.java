package joshie.progression.handlers;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import joshie.progression.api.criteria.ICriteria;
import joshie.progression.api.criteria.IReward;
import joshie.progression.api.criteria.ITab;
import joshie.progression.api.criteria.ITrigger;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.Callable;

public class APICache {
    private final Cache<UUID, ITrigger> triggerCache = CacheBuilder.newBuilder().maximumSize(8096).build();
    private final Cache<UUID, IReward> rewardCache = CacheBuilder.newBuilder().maximumSize(8096).build();
    private final HashMap<UUID, ICriteria> criteriaCache = new HashMap();
    private final HashMap<UUID, ITab> tabCache = new HashMap();

    public IReward getRewardFromUUID(final UUID uuid) {
        try {
            return rewardCache.get(uuid, new Callable<IReward>() {
                @Override
                public IReward call() throws Exception {
                    for (ICriteria criteria: criteriaCache.values()) {
                        for (IReward reward: criteria.getRewards()) {
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

    public ITrigger getTriggerFromUUID(final UUID uuid) {
        try {
            return triggerCache.get(uuid, new Callable<ITrigger>() {
                @Override
                public ITrigger call() throws Exception {
                    for (ICriteria criteria: getCriteria().values()) {
                        for (ITrigger trigger: criteria.getTriggers()) {
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
