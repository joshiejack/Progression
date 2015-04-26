package joshie.progression.helpers;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;

import joshie.progression.crafting.Crafter;
import joshie.progression.crafting.CrafterCreative;
import joshie.progression.crafting.CrafterHuman;
import joshie.progression.crafting.CraftingUnclaimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public class PlayerHelper {
	private static Cache<UUID, Crafter> crafters = CacheBuilder.newBuilder().maximumSize(64).build();

	public static Crafter getCrafterForUUID(final UUID uuid) {
	    Crafter crafter = getCrafterFromUUID(uuid);
	    return crafter == null? CraftingUnclaimed.INSTANCE: crafter;
	}
	
	public static Crafter getCrafterFromUUID(final UUID uuid) {
	  //If we are creative always jump to the creative profile, never cache it
        EntityPlayer player = PlayerHelper.getPlayerFromUUID(uuid);
        if (player != null && player.capabilities.isCreativeMode) {
            return CrafterCreative.INSTANCE;
        }
        
        try {
            return crafters.get(uuid, new Callable<Crafter>(){
                @Override
                public Crafter call() throws Exception {
                    return new CrafterHuman(uuid);
                }
            });
        } catch (Exception e) { return CraftingUnclaimed.INSTANCE; }
	}
	
	public static UUID getUUIDForPlayer(EntityPlayer player) {
		return EntityPlayer.func_146094_a(player.getGameProfile());
	}

	public static EntityPlayer getPlayerFromUUID(UUID uuid) {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) return ClientHelper.getPlayer();
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
