package joshie.crafting;

import java.util.UUID;

import joshie.crafting.api.CraftingAPI;
import joshie.crafting.helpers.ClientHelper;
import joshie.crafting.helpers.PlayerHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

public class CraftingEventsHandler {	
	@SubscribeEvent
	public void onCraft(ItemCraftedEvent event) {
		CraftingAPI.registry.fireTrigger(PlayerHelper.getUUIDForPlayer(event.player), CraftingCommon.triggerCrafting.getTypeName(), event.crafting.copy());
	}
	
	@SubscribeEvent
	public void onPlayerLoggedIn(PlayerLoggedInEvent event) {
		UUID uuid = PlayerHelper.getUUIDForPlayer(event.player);
		EntityPlayerMP player = (EntityPlayerMP)event.player;
		CraftingAPI.players.getServerPlayer(uuid).getMappings().syncToClient(player);
	}
	
	@SubscribeEvent
	public void onDeath(LivingDeathEvent event) {
		Entity source = event.source.getSourceOfDamage();
		if (source != null && source instanceof EntityPlayer) {
			CraftingAPI.registry.fireTrigger(PlayerHelper.getUUIDForPlayer((EntityPlayer)event.source.getSourceOfDamage()), CraftingCommon.triggerKill.getTypeName(), EntityList.getEntityString(event.entity));
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
