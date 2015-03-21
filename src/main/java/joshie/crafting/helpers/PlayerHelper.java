package joshie.crafting.helpers;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;

import joshie.crafting.api.crafting.ICrafter;
import joshie.crafting.implementation.crafters.CrafterHuman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import cpw.mods.fml.common.FMLCommonHandler;

public class PlayerHelper {
	private static Cache<UUID, ICrafter> crafters = CacheBuilder.newBuilder().maximumSize(64).build();

	public static ICrafter getCrafterForUUID(final UUID uuid) {
		try {
			return crafters.get(uuid, new Callable<ICrafter>(){
				@Override
				public ICrafter call() throws Exception {
					return new CrafterHuman(uuid);
				}
			});
		} catch (Exception e) { return null; }
	}
	
	public static ICrafter getCrafterForPlayer(final EntityPlayer player) {
		return getCrafterForUUID(getUUIDForPlayer(player));
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
}
