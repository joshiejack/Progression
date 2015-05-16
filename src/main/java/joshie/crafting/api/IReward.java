package joshie.crafting.api;

import java.util.List;
import java.util.UUID;

import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.eventhandler.Event.Result;

/** A reward is what you obtain from completing a full set
 *  of triggers, E.g. Ability to craft an item, Extra Speed */
public interface IReward extends IRewardType {
	/** Called when a condition has been met. This is ONLY Called ClientSide.
	 *  The reward should be given to the player whose uuid matches. */
	public void reward(UUID uuid);
	
	/** Called when this reward is added to a criteria **/
	public void onAdded();
	
	/** Called when the criteria associated with this reward is deleted **/
    public void onRemoved();

	/** Called by the ingame editor to grab an itemstack
	 *  that should be display to represent this reward, 
	 *  the research item, will have the icons, it's about
	 *  getting one with the right NBT data, or another **/
    public ItemStack getIcon();

    /** Draw the reward in the editor **/
    public void draw(int mouseX, int mouseY, int xPos);

    /** Whenever this reward is clicked on, Returns true if it should be deleted **/
    public Result onClicked();

    /** Gets the criteria associated with this reward **/
    public ICriteria getCriteria();
    
    /** Sets the criteria associated with this reward **/
    public IReward setCriteria(ICriteria criteria);

    public void addTooltip(List list);
}
