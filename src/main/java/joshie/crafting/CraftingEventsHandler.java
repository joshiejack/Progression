package joshie.crafting;

import java.util.UUID;

import joshie.crafting.api.CraftingAPI;
import joshie.crafting.api.crafting.CraftingType;
import joshie.crafting.api.crafting.ICrafter;
import joshie.crafting.helpers.PlayerHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

public class CraftingEventsHandler {		
	@SubscribeEvent
	public void onPlayerLoggedIn(PlayerLoggedInEvent event) {
		UUID uuid = PlayerHelper.getUUIDForPlayer(event.player);
		EntityPlayerMP player = (EntityPlayerMP)event.player;
		CraftingAPI.players.getServerPlayer(uuid).getMappings().syncToClient(player);
	}
	
	public static boolean checkAndCancelEvent(PlayerEvent event) {
		if (event.entityPlayer.getCurrentEquippedItem() == null) return true;
		EntityPlayer player = event.entityPlayer;
		ICrafter crafter = PlayerHelper.getCrafterForUUID(PlayerHelper.getUUIDForPlayer(player));
		if (!crafter.canCraftItem(CraftingType.CRAFTING, player.getCurrentEquippedItem())) {
			event.setCanceled(true);
			return false;
		} else return true;
	}
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onPlayerInteract(EntityInteractEvent event) {
		checkAndCancelEvent(event);
	}
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onPlayerInteract(PlayerInteractEvent event) {
		checkAndCancelEvent(event);
	}
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onPlayerAttack(AttackEntityEvent event) {
		checkAndCancelEvent(event);
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onItemTooltipEvent(ItemTooltipEvent event) {
		ICrafter crafter = PlayerHelper.getCrafterForUUID(PlayerHelper.getUUIDForPlayer(event.entityPlayer));
		if (!crafter.canCraftItem(CraftingType.CRAFTING, event.itemStack)) {
			event.toolTip.clear();
			event.toolTip.add("LOCKED");
		}
	}
}
