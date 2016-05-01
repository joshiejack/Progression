package joshie.progression.crafting.actions;

import joshie.progression.crafting.ActionType;
import joshie.progression.crafting.Crafter;
import joshie.progression.crafting.CraftingRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ActionGeneral extends ActionForgeEvent {
    public static final ActionGeneral INSTANCE = new ActionGeneral();

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onPlayerAttack(AttackEntityEvent event) {
        checkAndCancelEvent(event);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(EntityInteractEvent event) {
        checkAndCancelEvent(event);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        checkAndCancelEvent(event);
    }

    public boolean checkAndCancelEvent(net.minecraftforge.event.entity.player.PlayerEvent event) {
        if (event.entityPlayer.getCurrentEquippedItem() == null) return true;
        EntityPlayer player = event.entityPlayer;
        Crafter crafter = CraftingRegistry.get(player.worldObj.isRemote).getCrafterFromPlayer(player);
        if (!crafter.canUseItemWithAction(event.entityPlayer.worldObj, ActionType.GENERAL, player.getCurrentEquippedItem())) {
            event.setCanceled(true);
            return false;
        } else return true;
    }
}
