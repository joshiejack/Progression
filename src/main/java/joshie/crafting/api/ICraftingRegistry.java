package joshie.crafting.api;

import java.util.Collection;
import java.util.UUID;

import joshie.crafting.Criteria;
import joshie.crafting.api.crafting.CraftingEvent.CraftingType;
import joshie.crafting.api.crafting.ICrafter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public interface ICraftingRegistry {	
	/** Returns a list of all the criteria that this item stack
	 *  Need to have been filled before it can be crafted. 
	 * @param type */
	public Collection<Criteria> getCraftingCriteria(CraftingType type, ItemStack stack);
	
	/** Returns the criteria required to use this **/
	public Collection<Criteria> getCraftUsageCriteria(CraftingType type, ItemStack stack);

	/** Adds a criteria to this item stack
	 *  The name is the unique name you gave to your criteria */
	public void addRequirement(CraftingType type, String mod, ItemStack stack, boolean matchDamage, boolean matchNBT, boolean usage, boolean crafting, Criteria criteria);
	
	/** Follow the above criteria but defaults to matching damage, and not matching nbt **/
	public void addRequirement(CraftingType type, ItemStack stack, Criteria criteria);
	
	/** Grab the crafter instance for this player **/
	public ICrafter getCrafterFromPlayer(EntityPlayer player);
	
	/** Grab the crafter instance for this tile entity **/
	public ICrafter getCrafterFromTile(TileEntity tile);

	/** Grab the crafter instane for this uuid **/
	public ICrafter getCrafterFromUUID(UUID uuid);
}
