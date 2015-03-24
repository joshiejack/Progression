package joshie.crafting.helpers;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;

import joshie.crafting.api.crafting.ICrafter;
import joshie.crafting.crafting.CrafterCreative;
import joshie.crafting.crafting.CrafterHuman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import cpw.mods.fml.common.FMLCommonHandler;

public class PlayerHelper {
	private static Cache<UUID, ICrafter> crafters = CacheBuilder.newBuilder().maximumSize(64).build();

	public static ICrafter getCrafterForUUID(final UUID uuid) {
		//If we are creative always jump to the creative profile, never cache it
		EntityPlayer player = PlayerHelper.getPlayerFromUUID(uuid);
		if (player != null && player.capabilities.isCreativeMode) {
			return new CrafterCreative();
		}
		
		try {
			return crafters.get(uuid, new Callable<ICrafter>(){
				@Override
				public ICrafter call() throws Exception {
					return new CrafterHuman(uuid);
				}
			});
		} catch (Exception e) { return null; }
	}
	
	public static UUID getUUIDForPlayer(EntityPlayer player) {
		return EntityPlayer.func_146094_a(player.getGameProfile());
	}

	public static EntityPlayer getPlayerFromUUID(UUID uuid) {
		for (EntityPlayer player : (List<EntityPlayer>) FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().playerEntityList) {
            if (getUUIDForPlayer(player).equals(uuid)) {
                return (EntityPlayerMP) player;
            }
        }
		
		return null;
	}
	
	public static List<EntityPlayer> getAllPlayers() {
		return (List<EntityPlayer>) FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().playerEntityList;
	}
}
