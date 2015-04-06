package joshie.crafting.player;

import java.util.HashMap;
import java.util.UUID;

import joshie.crafting.CraftingMod;
import joshie.crafting.api.CraftingAPI;
import joshie.crafting.api.IPlayerData;
import joshie.crafting.api.IPlayerDataClient;
import joshie.crafting.api.IPlayerDataServer;
import joshie.crafting.api.IPlayerTracker;
import joshie.crafting.helpers.PlayerHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.event.world.BlockEvent.PlaceEvent;

import com.google.common.collect.Maps;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;

public class PlayerTracker implements IPlayerTracker {
	private final HashMap<TileEntity, UUID> owners = Maps.newHashMap();
	
	@Override
	public IPlayerData getPlayerData(UUID uuid) {
		return FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT? PlayerDataClient.getInstance(): CraftingMod.data.getServerPlayer(uuid);
	}
	
    @Override
    public IPlayerData getPlayerData(EntityPlayer player) {
        return getPlayerData(PlayerHelper.getUUIDForPlayer(player));
    }

	@Override
	public IPlayerDataClient getClientPlayer() {
		return PlayerDataClient.getInstance();
	}

	@Override
	public IPlayerDataServer getServerPlayer(UUID uuid) {
		return CraftingMod.data.getServerPlayer(uuid);
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onBlockPlaced(PlaceEvent event) {
		TileEntity tile = event.world.getTileEntity(event.x, event.y, event.z);
		if (event.player != null) {
			setTileOwner(tile, PlayerHelper.getUUIDForPlayer(event.player));
		}
	}
	
	@Override
	public IPlayerData getTileOwner(TileEntity tile) {
		return getPlayerData(owners.get(tile));
	}

	@Override
	public void setTileOwner(TileEntity tile, UUID owner) {
		owners.put(tile, owner);
	}
	
	//Called VIA ASM to READ Additional TileData
	public static void readFromNBT(TileEntity tile, NBTTagCompound nbt) {		
		UUID uuid = null;
		if (nbt.hasKey(MOST)) {
			uuid = new UUID(nbt.getLong(MOST), nbt.getLong(LEAST));
		}
		
		//Place the owner of this tile entity in the map
		if (uuid != null) {
			CraftingAPI.players.setTileOwner(tile, uuid);
		}
	}
	
	//Called VIA ASM to SAVE Additional TileData
	public static void writeToNBT(TileEntity tile, NBTTagCompound nbt) {		
		if (tile.getWorldObj() == null) return; //Don't continue if the world is null
		UUID uuid = CraftingAPI.players.getTileOwner(tile).getUUID();
		if (uuid != null) {
			nbt.setLong(MOST, uuid.getMostSignificantBits());
            nbt.setLong(LEAST, uuid.getLeastSignificantBits());
		}
	}
	
	private static final String MOST = "Crafting-Machine-Owner-Most";
	private static final String LEAST = "Crafting-Machine-Owner-Least";
}
