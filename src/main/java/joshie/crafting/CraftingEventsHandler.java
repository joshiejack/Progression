package joshie.crafting;

import java.util.Set;
import java.util.UUID;

import joshie.crafting.api.CraftingAPI;
import joshie.crafting.api.ICondition;
import joshie.crafting.api.IPlayerDataServer;
import joshie.crafting.api.IResearch;
import joshie.crafting.helpers.ClientHelper;
import joshie.crafting.helpers.PlayerHelper;
import joshie.crafting.network.PacketHandler;
import joshie.crafting.network.PacketSyncConditions;
import joshie.crafting.network.PacketSyncResearch;
import joshie.crafting.network.PacketSyncSpeed;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

public class CraftingEventsHandler {
	@SubscribeEvent
	public void onPlayerLoggedIn(PlayerLoggedInEvent event) {
		UUID uuid = PlayerHelper.getUUIDForPlayer(event.player);
		EntityPlayerMP player = (EntityPlayerMP)event.player;
		IPlayerDataServer data = CraftingAPI.players.getServerPlayer(uuid);
		Set<IResearch> research = data.getUnlockedResearch();
		Set<ICondition> conditions = data.getCompletedConditions();
		PacketHandler.sendToClient(new PacketSyncSpeed(data.getSpeed()), player);
		PacketHandler.sendToClient(new PacketSyncResearch(true, (IResearch[])research.toArray()), player); //Sync all researches to the client
		PacketHandler.sendToClient(new PacketSyncConditions(true, (ICondition[])conditions.toArray()), player); //Sync all conditions to the client
	}
	
	@SubscribeEvent
	public void onDeath(LivingDeathEvent event) {
		Entity source = event.source.getSourceOfDamage();
		if (source != null && source instanceof EntityPlayer) {
			CraftingAPI.players.getPlayerData(PlayerHelper.getUUIDForPlayer((EntityPlayer)source)).addKill(EntityList.getEntityString(event.entity));
		}
	}
	
	@SubscribeEvent
	public void onLivingUpdate(LivingUpdateEvent event) {
		if (event.entityLiving instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.entity;
            if (player.worldObj.isRemote) {
    			float speed = CraftingAPI.players.getPlayerData(null).getSpeed();
    			if (speed > 0 && player.onGround && !player.isInWater() && player.isSprinting() && ClientHelper.isForwardPressed()) {
    				player.moveFlying(0F, 1.0F, speed);
    			}
    		}
		}
	}
}
