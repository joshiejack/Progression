package joshie.progression.handlers;

import joshie.progression.api.criteria.ICriteria;
import joshie.progression.api.event.ActionEvent;
import joshie.progression.crafting.ActionType;
import joshie.progression.crafting.Crafter;
import joshie.progression.crafting.CraftingRegistry;
import joshie.progression.helpers.CraftingHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

import java.util.Set;

public class CraftingEvents {
    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerLoggedInEvent event) {
        RemappingHandler.onPlayerConnect((EntityPlayerMP) event.player);
    }
    
    @SubscribeEvent
    public void onAttemptToObtainItem(ActionEvent event) {
        if (event.stack == null) return;
        Crafter crafter = event.player != null ? CraftingRegistry.getCrafterFromPlayer(event.player) : CraftingRegistry.getCrafterFromTile(event.tile);
        if (crafter.canDoAnything()) return;
        if (!crafter.canUseItemWithAction(event.type, event.stack)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onItemTooltipEvent(ItemTooltipEvent event) {
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

    public static boolean isEventCancelled(EntityPlayer player, ActionType usageAction, ItemStack usageStack, ActionType craftingAction, ItemStack craftingStack) {
        if (!CraftingHelper.canPerformAction(usageAction, player, usageStack)) {
            return true;
        } else {
            if (!CraftingHelper.canPerformAction(craftingAction, player, craftingStack)) {
                return true;
            }
        }

        return false;
    }
}
