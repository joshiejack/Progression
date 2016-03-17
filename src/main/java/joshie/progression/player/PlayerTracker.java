package joshie.progression.player;

import java.util.HashMap;
import java.util.UUID;

import com.google.common.collect.Maps;

import joshie.progression.Progression;
import joshie.progression.helpers.PlayerHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.event.world.BlockEvent.PlaceEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PlayerTracker {
    private static final HashMap<TileEntity, UUID> owners = Maps.newHashMap();

    @SideOnly(Side.CLIENT)
    public static PlayerDataClient getClientPlayer() {
        return PlayerDataClient.getInstance();
    }

    public static PlayerDataServer getServerPlayer(EntityPlayer player) {
        return getServerPlayer(PlayerHelper.getUUIDForPlayer(player));
    }
    
    public static boolean reset(String username) {
        return Progression.data.reset(username);
    }
    
    public static PlayerDataServer getServerPlayer(UUID uuid) {
        return Progression.data.getServerPlayer(uuid);
    }
    
    public static PlayerDataCommon getPlayerData(UUID uuid) {
        return FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT? getClientPlayer() : getServerPlayer(uuid);
    }

    public static PlayerDataCommon getPlayerData(EntityPlayer player) {
        return getPlayerData(PlayerHelper.getUUIDForPlayer(player));
    }

    public static UUID getTileOwner(TileEntity tile) {
        return owners.get(tile);
    }

    public static void setTileOwner(TileEntity tile, UUID owner) {
        owners.put(tile, owner);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onBlockPlaced(PlaceEvent event) {
        TileEntity tile = event.world.getTileEntity(event.pos);
        if (event.player != null) {
            setTileOwner(tile, PlayerHelper.getUUIDForPlayer(event.player));
        }
    }

    //Called VIA ASM to READ Additional TileData
    public static void readFromNBT(TileEntity tile, NBTTagCompound nbt) {
        UUID uuid = null;
        if (nbt.hasKey(MOST)) {
            uuid = new UUID(nbt.getLong(MOST), nbt.getLong(LEAST));
        }

        //Place the owner of this tile entity in the map
        if (uuid != null) {
            PlayerTracker.setTileOwner(tile, uuid);
        }
    }

    //Called VIA ASM to SAVE Additional TileData
    public static void writeToNBT(TileEntity tile, NBTTagCompound nbt) {
        if (tile.getWorld() == null) return; //Don't continue if the world is null
        UUID uuid = PlayerTracker.getTileOwner(tile);
        if (uuid != null) {
            nbt.setLong(MOST, uuid.getMostSignificantBits());
            nbt.setLong(LEAST, uuid.getLeastSignificantBits());
        }
    }

    private static final String MOST = "Crafting-Machine-Owner-Most";
    private static final String LEAST = "Crafting-Machine-Owner-Least";
}
