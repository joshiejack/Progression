package joshie.progression.handlers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import joshie.progression.Progression;
import joshie.progression.api.ICriteria;
import joshie.progression.api.event.ActionEvent.CanObtainFromActionEvent;
import joshie.progression.api.event.ActionEvent.CanUseToPeformActionEvent;
import joshie.progression.crafting.ActionType;
import joshie.progression.crafting.Crafter;
import joshie.progression.crafting.CraftingRegistry;
import joshie.progression.gui.newversion.GuiCriteriaEditor;
import joshie.progression.helpers.CraftingHelper;
import joshie.progression.helpers.MCClientHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

public class CraftingEvents {
    @SubscribeEvent
    public void onAttemptToUseItemToPerformAction(CanUseToPeformActionEvent event) {
        if (event.stack == null) return;
        Crafter crafter = event.player != null ? CraftingRegistry.getCrafterFromPlayer(event.player) : CraftingRegistry.getCrafterFromTile(event.tile);
        if (crafter.canCraftWithAnything()) return;
        if (!crafter.canUseItemForCrafting(event.type, event.stack)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onAttemptToObtainItem(CanObtainFromActionEvent event) {
        if (event.stack == null) return;
        Crafter crafter = event.player != null ? CraftingRegistry.getCrafterFromPlayer(event.player) : CraftingRegistry.getCrafterFromTile(event.tile);
        if (crafter.canCraftAnything()) return;
        if (!crafter.canCraftItem(event.type, event.stack)) {
            event.setCanceled(true);
        }
    }

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
        if (!checkAndCancelEvent(event)) {
            Collection<ICriteria> requirements = CraftingRegistry.getRequirements(event.entityPlayer.getCurrentEquippedItem());
            if (requirements.size() > 0) {
                if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
                    for (ICriteria c : requirements) {
                        GuiCriteriaEditor.INSTANCE.setCriteria(c);
                        break;
                    }
                } else event.entityPlayer.openGui(Progression.instance, 1, null, 0, 0, 0);
            }
        }
    }

    static java.util.List<Crafter> fooList = new ArrayList<Crafter>();

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onItemTooltipEvent(ItemTooltipEvent event) {
        Crafter crafter = CraftingRegistry.getCrafterFromPlayer(MCClientHelper.getPlayer());
        if (!crafter.canCraftItem(ActionType.CRAFTING, event.itemStack)) {
            //TODO: Readd tooltips for things you can't craft
            boolean hasStuff = false;
            for (ActionType type : ActionType.values()) {
                Set<ICriteria> requirements = CraftingRegistry.getRequirements(type, event.itemStack);
                if (requirements.size() > 0) {
                    if (!hasStuff) {
                        event.toolTip.add("Currently Locked");
                        hasStuff = true;
                    }

                    event.toolTip.add(EnumChatFormatting.WHITE + type.getDisplayName());
                    for (ICriteria c : requirements) {
                        ((ICriteria) c).addTooltip(event.toolTip);
                    }
                }
            }

            if (hasStuff) {
                event.toolTip.add(EnumChatFormatting.AQUA + "Click for more info");
            }
        }
    }

    public static boolean isEventCancelled(EntityPlayer player, ActionType type, ItemStack usageStack, ItemStack craftingStack) {
        if (!CraftingHelper.canUseItemForCrafting(type, player, usageStack)) {
            return true;
        } else {
            if (!CraftingHelper.canCraftItem(type, player, craftingStack)) {
                return true;
            }
        }

        return false;
    }

    public static boolean checkAndCancelEvent(PlayerEvent event) {
        if (event.entityPlayer.getCurrentEquippedItem() == null) return true;
        EntityPlayer player = event.entityPlayer;
        Crafter crafter = CraftingRegistry.getCrafterFromPlayer(player);
        if (!crafter.canCraftItem(ActionType.CRAFTING, player.getCurrentEquippedItem())) {
            event.setCanceled(true);
            return false;
        } else return true;
    }
}
