package joshie.crafting.implementation;

import java.util.HashMap;
import java.util.UUID;

import joshie.crafting.api.IOwnerTracker;
import joshie.crafting.api.crafting.ICrafter;
import joshie.crafting.helpers.PlayerHelper;
import joshie.crafting.implementation.crafters.CraftingUnclaimed;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.event.world.BlockEvent.PlaceEvent;

import com.google.common.collect.Maps;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class OwnerTracker implements IOwnerTracker {
	private static final String MOST = "Crafting-Machine-Owner-Most";
	private static final String LEAST = "Crafting-Machine-Owner-Least";
	
	//A map of all the owner of this block
	private static final HashMap<TileEntity, UUID> owner = Maps.newHashMap();

	@Override
	public ICrafter getOwner(TileEntity tile) {		
		ICrafter crafter = PlayerHelper.getCrafterForUUID(owner.get(tile));
		return crafter == null? CraftingUnclaimed.INSTANCE: crafter;
	}

	@Override
	public void setOwner(TileEntity tile, UUID uuid) {
		owner.put(tile, uuid);
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST) //Set priority low to ensure the block is placed
	public void onBlockPlaced(PlaceEvent event) {
		TileEntity tile = event.world.getTileEntity(event.x, event.y, event.z);
		if (event.player != null) {
			setOwner(tile, PlayerHelper.getUUIDForPlayer(event.player));
		}
	}

	//Read the owner of this tile
	public static void readFromNBT(TileEntity tile, NBTTagCompound nbt) {
		UUID uuid = null;
		if (nbt.hasKey(MOST)) {
			uuid = new UUID(nbt.getLong(MOST), nbt.getLong(LEAST));
		}
		
		//Place the owner of this tile entity in the map
		owner.put(tile, uuid);
	}
	
	public static void writeToNBT(TileEntity tile, NBTTagCompound nbt) {
		UUID uuid = owner.get(tile);
		if (uuid != null) {
			nbt.setLong(MOST, uuid.getMostSignificantBits());
            nbt.setLong(LEAST, uuid.getLeastSignificantBits());
		}
	}
}
