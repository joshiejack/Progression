package joshie.progression.handlers;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import joshie.progression.api.criteria.IProgressionCriteria;
import joshie.progression.api.criteria.IProgressionReward;
import joshie.progression.api.criteria.IProgressionTab;
import joshie.progression.api.criteria.IProgressionTrigger;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.Callable;

public class APICache {
    private final Cache<UUID, IProgressionTrigger> triggerCache = CacheBuilder.newBuilder().maximumSize(8096).build();
    private final Cache<UUID, IProgressionReward> rewardCache = CacheBuilder.newBuilder().maximumSize(8096).build();
    private final HashMap<UUID, IProgressionCriteria> criteriaCache = new HashMap();
    private final HashMap<UUID, IProgressionTab> tabCache = new HashMap();

    public IProgressionReward getRewardFromUUID(final UUID uuid) {
        try {
            return rewardCache.get(uuid, new Callable<IProgressionReward>() {
                @Override
                public IProgressionReward call() throws Exception {
                    for (IProgressionCriteria criteria: criteriaCache.values()) {
                        for (IProgressionReward reward: criteria.getRewards()) {
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

    public IProgressionTrigger getTriggerFromUUID(final UUID uuid) {
        try {
            return triggerCache.get(uuid, new Callable<IProgressionTrigger>() {
                @Override
                public IProgressionTrigger call() throws Exception {
                    for (IProgressionCriteria criteria: getCriteria().values()) {
                        for (IProgressionTrigger trigger: criteria.getTriggers()) {
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

    public HashMap<UUID,IProgressionCriteria> getCriteria() {
        return criteriaCache;
    }

    public HashMap<UUID,IProgressionTab> getTabs() {
        return tabCache;
    }
}
