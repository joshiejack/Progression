package joshie.crafting.api;

import java.util.Collection;

import joshie.crafting.api.crafting.CraftingType;
import net.minecraft.item.ItemStack;

public interface ICraftingRegistry {
	/** Called to initalise the registry **/
	public ICraftingRegistry init();
	
	/** Returns a list of all the criteria that this item stack
	 *  Need to have been filled before it can be crafted. 
	 * @param type */
	public Collection<ICriteria> getCraftingCriteria(CraftingType type, ItemStack stack);
	
	/** Returns the criteria required to use this **/
	public Collection<ICriteria> getCraftUsageCriteria(CraftingType type, ItemStack stack);

	/** Adds a criteria to this item stack
	 *  The name is the unique name you gave to your criteria */
	public void addRequirement(CraftingType type, ItemStack stack, boolean matchDamage, boolean matchNBT, boolean usage, boolean crafting, ICriteria criteria);
	
	/** Follow the above criteria but defaults to matching damage, and not matching nbt **/
	public void addRequirement(CraftingType type, ItemStack stack, ICriteria criteria);
}
