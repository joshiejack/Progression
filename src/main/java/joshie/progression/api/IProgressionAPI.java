package joshie.progression.api;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.eventhandler.Event.Result;

/** The registry is where you register new types of rewards, triggers and
 *  conditions, and the criteria. All triggers and rewards should be registered before any criteria, and conditions before triggers */
public interface IProgressionAPI {
	/** Fires all triggers, of the type specified, Triggers should only be fired
	 *  On the server side.
	 *  @return		returns allow if the triggers suceeded
	 *              returns deny if one of the triggers cancelled the event
	 *              returns default if nothing happened **/
	public Result fireTrigger(UUID uuid, String trigger, Object... data);
	
	/** Convenience method **/
	public Result fireTrigger(EntityPlayer player, String trigger, Object... data);
	
	/** Register a condition with the registry **/
	public IConditionType registerConditionType(IConditionType reward);
	
	/** Register a trigger with the registry **/
	public ITriggerType registerTriggerType(ITriggerType trigger);
	
	/** Register a reward with the registry **/
	public IRewardType registerRewardType(IRewardType reward);
	
	/** Register a new crafting type **/
	public void registerActionType(String unlocalised);
	
	/** Convenience method for calling, which calls the crafting events
	 *  @param     the type of action, either furnace or crafting normally, could be breakBlock
	 *             if you wish to call this from a new machine, register it
	 *             on startup with the method above.
	 *  @param     the stack you are attempting to craft/use/whatever
	 *  @param     either a player, or a tile entity **/
	public boolean canObtainFromAction(String actionType, ItemStack stack, Object tileOrPlayer);
	
	/** Similiar to above, but this is whether the item can be used to perform, the action
	 *  e.g. for crafting it would be whether you can use the item for crafting, or with blocks
	 *  it's whether you can use x item to harvest a block.
	 *  @param     the type of action
	 *  @param     the stack attempting to be used
	 *  @param     the player or tile tile entity  */
	public boolean canUseToPerformAction(String actionType, ItemStack stack, Object tileOrPlayer);
}
