package joshie.crafting;

import joshie.crafting.api.CraftingAPI;
import joshie.crafting.api.crafting.CraftingEvent.CanCraftItemEvent;
import joshie.crafting.api.crafting.CraftingEvent.CanRepairItemEvent;
import joshie.crafting.api.crafting.CraftingEvent.CanUseItemCraftingEvent;
import joshie.crafting.api.crafting.CraftingEvent.CraftingType;
import joshie.crafting.api.crafting.ICrafter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

/** Sync data, and make locked items useless **/
public class CraftingEventsHandler {
    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerLoggedInEvent event) {
        CraftingRemapper.onPlayerConnect((EntityPlayerMP) event.player);
    }

    public static boolean checkAndCancelEvent(PlayerEvent event) {
        if (event.entityPlayer.getCurrentEquippedItem() == null) return true;
        EntityPlayer player = event.entityPlayer;
        ICrafter crafter = CraftingAPI.crafting.getCrafterFromPlayer(player);
        if (!crafter.canCraftItem(CraftingType.CRAFTING, player.getCurrentEquippedItem())) {
            event.setCanceled(true);
            return false;
        } else return true;
    }

    private boolean isBook(ItemStack stack) {
        if (CraftingMod.options.editor) {
            if (stack != null) {
                if (stack.getItem() == Items.book) {
                    return true;
                }
            }
        }

        return false;
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (isBook(event.entityPlayer.getCurrentEquippedItem())) {
            event.entityPlayer.openGui(CraftingMod.instance, 0, null, 0, 0, 0);
        }

        checkAndCancelEvent(event);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onPlayerAttack(AttackEntityEvent event) {
        checkAndCancelEvent(event);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(EntityInteractEvent event) {
        checkAndCancelEvent(event);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onItemTooltipEvent(ItemTooltipEvent event) {
        ICrafter crafter = CraftingAPI.crafting.getCrafterFromPlayer(event.entityPlayer);
        if (!crafter.canCraftItem(CraftingType.CRAFTING, event.itemStack)) {
            event.toolTip.clear();
            event.toolTip.add("LOCKED");
        }

        if (isBook(event.itemStack)) {
            event.toolTip.add("Right click me to access");
            event.toolTip.add("\"Craft Control\" editor.");
        }
    }

    @SubscribeEvent
    public void onAttemptToUseItemForCrafting(CanUseItemCraftingEvent event) {
        if (event.stack == null) return;
        ICrafter crafter = event.player != null ? CraftingAPI.crafting.getCrafterFromPlayer(event.player) : CraftingAPI.crafting.getCrafterFromTile(event.tile);
        if (crafter.canCraftWithAnything()) return;
        if (!crafter.canUseItemForCrafting(event.type, event.stack)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onAttemptToRepairItem(CanRepairItemEvent event) {
        ICrafter crafter = event.player != null ? CraftingAPI.crafting.getCrafterFromPlayer(event.player) : CraftingAPI.crafting.getCrafterFromTile(event.tile);
        if (!crafter.canRepairItem(event.stack)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onAttemptToCraftItem(CanCraftItemEvent event) {
        ICrafter crafter = event.player != null ? CraftingAPI.crafting.getCrafterFromPlayer(event.player) : CraftingAPI.crafting.getCrafterFromTile(event.tile);
        if (!crafter.canCraftItem(event.type, event.stack)) {
            event.setCanceled(true);
        }
    }
}
