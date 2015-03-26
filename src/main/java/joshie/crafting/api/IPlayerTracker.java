package joshie.crafting.api;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

public interface IPlayerTracker {
	/** returns the player data based on this uuid **/
	public IPlayerData getPlayerData(UUID uuid);
	
	/** returns the client specific player data **/
	public IPlayerDataClient getClientPlayer();
	
	/** returns the server specific player data **/
	public IPlayerDataServer getServerPlayer(UUID uuid);

	/** returns the player data based on this tile entity **/
	public IPlayerData getTileOwner(TileEntity tile);
	
	/** Sets the owner of this tile entity **/
	public void setTileOwner(TileEntity tile, UUID owner);

	/** Convenience method **/
    public IPlayerData getPlayerData(EntityPlayer player);
}
