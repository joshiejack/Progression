package joshie.crafting.api;

import java.util.UUID;

import joshie.crafting.api.crafting.CraftingType;
import net.minecraft.item.ItemStack;

public interface ICondition {
	/** Creates a new instance of this condition
	 * 
	 * @param 			data loaded from json, as a string */
	public ICondition newInstance(String data);

	/** Called directly after newInstance **/
	public ICondition setCraftingType(String craftType);
	
	/** Returns whether this condition has been met
	 * @param 			the uuid
	 * @return			true if the condition is met, false if it is not */
	public boolean isMet(UUID uuid);
	
	/** Returns true if the crafting type is correct **/
	public boolean isCorrectCraftingType(CraftingType type);

	/** This is called whenever a condition is added to an itemstack
	 *  
	 * @param 			the stack the condition was added to */
	public void onAdded(ItemStack stack);

	/** Returns the name of this condition
	 *  This is what is used when adding a new one from json. */
	public String getName();
}
