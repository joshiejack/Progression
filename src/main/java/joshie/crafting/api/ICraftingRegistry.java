package joshie.crafting.api;

import java.util.Collection;

import joshie.crafting.api.crafting.CraftingType;
import net.minecraft.item.ItemStack;

public interface ICraftingRegistry {
	/** Called to initalise the registry **/
	public ICraftingRegistry init();
	
	/** Returns a list of all the conditions that this item stack
	 *  Need to have been filled before it can be crafted. 
	 * @param type */
	public Collection<ICriteria> getConditions(CraftingType type, ItemStack stack);

	/** Adds a condition to this item stack
	 *  The name is the unique name you gave to your condition */
	public void addRequirement(CraftingType type, ItemStack stack, boolean matchDamage, boolean matchNBT, ICriteria criteria);
	
	/** Follow the above condition but defaults to matching damage, and not matching nbt **/
	public void addRequirement(CraftingType type, ItemStack stack, ICriteria criteria);
}
