package joshie.progression.helpers;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import joshie.progression.crafting.Crafter;
import joshie.progression.crafting.CrafterCreative;
import joshie.progression.crafting.CrafterHuman;
import joshie.progression.crafting.CraftingUnclaimed;
import joshie.progression.json.Options;
import joshie.progression.player.PlayerTeam;
import joshie.progression.player.PlayerTracker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Callable;

public class PlayerHelper {
    private static Cache<UUID, Crafter> crafters = CacheBuilder.newBuilder().maximumSize(64).build();

    public static Crafter getCrafterForUUID(final boolean isClient, final UUID uuid) {
        Crafter crafter = getCrafterFromUUID(isClient, uuid);
        return crafter == null ? CraftingUnclaimed.INSTANCE : crafter;
    }

    public static Crafter getCrafterFromUUID(final boolean isClient, final UUID uuid) {
        //If we are creative always jump to the creative profile, never cache it
        EntityPlayer player = PlayerHelper.getPlayerFromUUID(isClient, uuid);
        if (Options.getSettings().craftAnythingCreative && player != null && player.capabilities.isCreativeMode) {
            return CrafterCreative.INSTANCE;
        }

        try {
            return crafters.get(uuid, new Callable<Crafter>() {
                @Override
                public Crafter call() throws Exception {
                    return new CrafterHuman(uuid);
                }
            });
        } catch (Exception e) {
            return CraftingUnclaimed.INSTANCE;
        }
    }

    public static UUID getUUIDForPlayer(EntityPlayer player) {
        return EntityPlayer.getUUID(player.getGameProfile());
    }

    /** Should only ever be called serverside **/
    public static Set<EntityPlayerMP> getPlayersFromUUID(UUID uuid) {
        Set<EntityPlayerMP> list = new LinkedHashSet();
        PlayerTeam team = PlayerTracker.getServerPlayer(uuid).getTeam();
        for (EntityPlayerMP player : getAllPlayers()) {
            /** Add the Owner **/
            if (getUUIDForPlayer(player).equals(team.getOwner())) {
                list.add((EntityPlayerMP) player);
            }

            if (team.giveMultipleRewards()) {
                /** Add the Team Members **/
                for (UUID member : team.getMembers()) {
                    if (getUUIDForPlayer(player).equals(member)) {
                        list.add((EntityPlayerMP) player);
                    }
                }
            }
        }

        return list;
    }

    public static EntityPlayer getPlayerFromUUID(boolean isClient, UUID uuid) {
        if (isClient) return MCClientHelper.getPlayer();
        for (EntityPlayerMP player : getAllPlayers()) {
            if (getUUIDForPlayer(player).equals(uuid)) {
                return player;
            }
        }

        return null;
    }

    public static List<EntityPlayerMP> getAllPlayers() {
        return (List<EntityPlayerMP>) FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().playerEntityList;
    }

    @SideOnly(Side.CLIENT)
    public static UUID getClientUUID() {
        return getUUIDForPlayer(MCClientHelper.getPlayer());
    }
}
