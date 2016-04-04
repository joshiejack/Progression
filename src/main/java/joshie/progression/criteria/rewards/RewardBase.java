package joshie.progression.criteria.rewards;

import joshie.progression.api.criteria.IReward;
import joshie.progression.api.criteria.IRewardProvider;
import net.minecraft.entity.player.EntityPlayerMP;

public abstract class RewardBase implements IReward {
    private IRewardProvider provider;

    @Override
    public void setProvider(IRewardProvider provider) {
        this.provider = provider;
    }

    @Override
    public IRewardProvider getProvider() {
        return provider;
    }

    @Override
    public void onAdded() {}

    @Override
    public void onRemoved() {}

    @Override
    public boolean shouldRunOnce() {
        return false;
    }
    
    @Override
    public void reward(EntityPlayerMP player) {}
}
