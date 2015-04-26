package joshie.progression.api;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;

/** The registry is where you register new types of rewards, triggers and
 *  conditions, and the criteria. All triggers and rewards should be registered before any criteria, and conditions before triggers */
public interface IRegistry {
	/** Fires all triggers, of the type specified, Triggers should only be fired
	 *  On the server side.
	 *  @return		returns true if just one of the triggers suceeded **/
	public boolean fireTrigger(UUID uuid, String trigger, Object... data);
	
	/** Convenience method **/
	public boolean fireTrigger(EntityPlayer player, String trigger, Object... data);
	
	/** Register a condition with the registry **/
	public IConditionType registerConditionType(IConditionType reward);
	
	/** Register a trigger with the registry **/
	public ITriggerType registerTriggerType(ITriggerType trigger);
	
	/** Register a reward with the registry **/
	public IRewardType registerRewardType(IRewardType reward);
	
	/** Register a new crafting type **/
	public void registerCraftingType(String unlocalised);
}
