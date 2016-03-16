package joshie.progression.api;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Event.Result;

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
	
	/** Call this when firing triggers on the server, the data objects you can use are limited to the following
	 *  boolean, byte, short, int, long, double, float, String
	 *  ItemStack, NBTTagCompound, Criteria, Block, Item, Entity 
	 *  If you want to be able to handle your own items, send them in one of these forms
	 *  Then use the ICustomDataBuilder to build your objects again from the data you passed,
	 *  Keep note that */
    public void fireTriggerClientside(String trigger, Object... data);
    
    /** Register an ICustomDataBuilder
     *  Keep in mind you need to pass the same string
     *  to fireTrigger for this to work correctly. **/
    public void registerCustomDataBuilder(String trigger, ICustomDataBuilder builder);
	
	/** Register a condition with the registry **/
	public IConditionType registerConditionType(IConditionType reward);
	
	/** Register a trigger with the registry **/
	public ITriggerType registerTriggerType(ITriggerType trigger);
	
	/** Register a reward with the registry **/
	public IRewardType registerRewardType(IRewardType reward);
	
	/** Register a filter type with the registry **/
	public IFilter registerItemFilter(IFilter filter);
	
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
