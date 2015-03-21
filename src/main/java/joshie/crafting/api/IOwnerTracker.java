package joshie.crafting.api;

import java.util.UUID;

import joshie.crafting.api.crafting.ICrafter;
import net.minecraft.tileentity.TileEntity;

public interface IOwnerTracker {
	/** Returns the owner of this tile entity
	 * 
	 * @param 			the tile
	 * @return 			the owner */
	public ICrafter getOwner(TileEntity tile);
	
	/** Sets the owner of this tile entity
	 * 
	 * @param 			the tile
	 * @param			the uuid of the owner */
	public void setOwner(TileEntity tile, UUID uuid);
}
