package joshie.progression.api.criteria;

import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.UUID;

public interface IProgressionReward extends IFieldProvider {
    /** Associates this reward type with the criteria
     *  Most reward types will not need access to this. **/
    public void setCriteria(IProgressionCriteria criteria, UUID uuid);

    /** Gives the reward to this UUID **/
    public void reward(UUID uuid);

    /** Called when the reward is added **/
    public void onAdded();

    /** Called when the reward is removed**/
    public void onRemoved();

    /** Return the itemstack that represents this reward in tree editor view **/
    public ItemStack getIcon();

    /** Add a tooltip for display in the tree view **/
    public void addTooltip(List list);
}
