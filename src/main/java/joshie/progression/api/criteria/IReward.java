package joshie.progression.api.criteria;

import net.minecraft.entity.player.EntityPlayerMP;

public interface IReward extends IRule<IRewardProvider> {
        /** Rewards this player, Called server side only **/
    public void reward(EntityPlayerMP player);

    /** Called when the reward is added **/
    public void onAdded();

    /** Called when the reward is removed**/
    public void onRemoved();

    /** Return true if this reward should only ever execute once for the team **/
    public boolean shouldRunOnce();
}
