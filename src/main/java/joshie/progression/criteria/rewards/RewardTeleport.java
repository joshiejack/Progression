package joshie.progression.criteria.rewards;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import joshie.progression.Progression;
import joshie.progression.helpers.PlayerHelper;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.network.play.server.S1DPacketEntityEffect;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class RewardTeleport extends RewardBase {
    public int dimension = 0;
    public double x = 0;
    public double y = 64;
    public double z = 0;

    public RewardTeleport() {
        super(new ItemStack(Items.ender_pearl), "teleport", 0xFFDDDDDD);
    }

    @Override
    public void reward(UUID uuid) {
        List<EntityPlayerMP> players = PlayerHelper.getPlayersFromUUID(uuid);
        World world = DimensionManager.getWorld(dimension);
        if (world == null) return; //Don't teleport to dimensions that don't exist
        for (EntityPlayerMP player : players) {
            if (player.dimension != dimension) {
                int oldDim = player.dimension;
                ServerConfigurationManager manager = player.mcServer.getConfigurationManager();
                WorldServer worldserver = manager.getServerInstance().worldServerForDimension(player.dimension);
                player.dimension = dimension;
                WorldServer worldserver1 = manager.getServerInstance().worldServerForDimension(player.dimension);
                player.playerNetServerHandler.sendPacket(new S07PacketRespawn(player.dimension, player.worldObj.getDifficulty(), player.worldObj.getWorldInfo().getTerrainType(), player.theItemInWorldManager.getGameType()));
                worldserver.removePlayerEntityDangerously(player);
                if (player.riddenByEntity != null) {
                    player.riddenByEntity.mountEntity(null);
                }

                if (player.ridingEntity != null) {
                    player.mountEntity(null);
                }

                player.isDead = false;
                WorldProvider pOld = worldserver.provider;
                WorldProvider pNew = worldserver1.provider;
                double moveFactor = pOld.getMovementFactor() / pNew.getMovementFactor();
                double x = player.posX * moveFactor;
                double z = player.posZ * moveFactor;

                worldserver.theProfiler.startSection("placing");
                x = MathHelper.clamp_double(x, -29999872, 29999872);
                z = MathHelper.clamp_double(z, -29999872, 29999872);

                if (player.isEntityAlive()) {
                    player.setLocationAndAngles(x, player.posY, z, player.rotationYaw, player.rotationPitch);
                    worldserver1.spawnEntityInWorld(player);
                    worldserver1.updateEntityWithOptionalForce(player, false);
                }

                worldserver.theProfiler.endSection();

                player.setWorld(worldserver1);
                manager.preparePlayer(player, worldserver);
                player.playerNetServerHandler.setPlayerLocation(player.posX, player.posY, player.posZ, player.rotationYaw, player.rotationPitch);
                player.theItemInWorldManager.setWorld(worldserver1);
                manager.updateTimeAndWeatherForPlayer(player, worldserver1);
                manager.syncPlayerInventory(player);

                Iterator<PotionEffect> iterator = player.getActivePotionEffects().iterator();
                while (iterator.hasNext()) {
                    PotionEffect potioneffect = iterator.next();
                    player.playerNetServerHandler.sendPacket(new S1DPacketEntityEffect(player.getEntityId(), potioneffect));
                }

                FMLCommonHandler.instance().firePlayerChangedDimensionEvent(player, oldDim, dimension);
            }

            player.setPositionAndUpdate(x, y, z);
        }
    }

    @Override
    public void addTooltip(List list) {
        list.add(EnumChatFormatting.WHITE + Progression.translate("teleport"));
    }
}
