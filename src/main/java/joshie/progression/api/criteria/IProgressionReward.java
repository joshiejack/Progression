package joshie.progression.api.criteria;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.UUID;

public interface IProgressionReward extends ICanHaveEvents {
    /** Associates this reward type with the criteria
     *  Most reward types will not need access to this. **/
    public void setCriteria(IProgressionCriteria criteria, UUID uuid);

    /** Rewards this player, Called server side only **/
    public void reward(EntityPlayerMP player);

    /** Called when the reward is added **/
    public void onAdded();

    /** Called when the reward is removed**/
    public void onRemoved();

    /** Return the itemstack that represents this reward in tree editor view **/
    public ItemStack getIcon();

    /** Add a tooltip for display in the tree view **/
    public void addTooltip(List list);

    /** Return true if this reward should only ever execute once for the team **/
    public boolean shouldRunOnce();

    /** Returns true if this rewards should be automatically given,
     *  Ideally should be configurable for the player for each reward. **/
    public boolean isAutomatic();

    /** return the criteria this reward is attached to **/
    public IProgressionCriteria getCriteria();
}
