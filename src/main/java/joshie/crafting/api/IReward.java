package joshie.crafting.api;

import java.util.UUID;

import net.minecraft.item.ItemStack;

/** A reward is what you obtain from completing a full set
 *  of triggers, E.g. Ability to craft an item, Extra Speed */
public interface IReward extends IHasUniqueName, IRewardType {
	/** Called when a condition has been met. This is ONLY Called ClientSide.
	 *  The reward should be given to the player whose uuid matches. */
	public void reward(UUID uuid);
	
	/** Called when this reward is added to a criteria **/
	public void onAdded(ICriteria criteria);

	/** Called by the ingame editor to grab an itemstack
	 *  that should be display to represent this reward, 
	 *  the research item, will have the icons, it's about
	 *  getting one with the right NBT data, or another **/
    public ItemStack getIcon();
}
