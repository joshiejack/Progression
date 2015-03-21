package joshie.crafting.api;

import java.util.List;

import net.minecraft.item.ItemStack;

public interface IConditionHandler {
	/** Registers a condition **/
	public void registerCondition(ICondition condition);
	
	/** Returns a list of all the conditions required to craft this item
	 *  @param			the stack to get the conditions for
	 *  @return			a list of all conditions, can be an empty list **/
	public List<ICondition> getConditions(ItemStack stack);
	
	/** Registers an itemstack with the conditions
	 *  @param			the stack to add conditions to
	 *  @param			the conditions to add to this stack **/
	public void addCondition(ItemStack stack, ICondition... conditions);

	/** Fetches the condition based on this name
	 * 
	 * @param 			conditions name */
	public ICondition getConditionFromName(String condition);
}
