package joshie.progression.player;

import java.util.HashMap;
import java.util.HashSet;
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
    private static final HashSet<TileEntity> loaded = new HashSet();

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
        return FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT ? getClientPlayer() : getServerPlayer(uuid);
    }

    public static PlayerDataCommon getPlayerData(EntityPlayer player) {
        return getPlayerData(PlayerHelper.getUUIDForPlayer(player));
    }

    public static UUID getTileOwner(TileEntity tile) {
        UUID uuid = owners.get(tile);
        if (uuid == null && !loaded.contains(tile)) {
            //Attempt to load it from the tile
            if (tile.getTileData().hasKey("Progression")) {
                String id = tile.getTileData().getCompoundTag("Progression").getString("Owner");
                uuid = UUID.fromString(id);
                if (uuid != null) {
                    owners.put(tile, uuid);
                }
            }
            
            loaded.add(tile);
        }

        return uuid;
    }

    public static void setTileOwner(TileEntity tile, UUID owner) {
        owners.put(tile, owner);
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("Owner", owner.toString());
        tile.getTileData().setTag("Progression", tag);
        tile.markDirty();
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onBlockPlaced(PlaceEvent event) {
        TileEntity tile = event.world.getTileEntity(event.pos);
        if (event.player != null && tile != null) {
            setTileOwner(tile, PlayerHelper.getUUIDForPlayer(event.player));
        }
    }
}
