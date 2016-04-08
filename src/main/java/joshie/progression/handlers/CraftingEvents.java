package joshie.progression.handlers;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.HashMultimap;
import joshie.progression.Progression;
import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.criteria.ICriteria;
import joshie.progression.api.event.ActionEvent;
import joshie.progression.crafting.ActionType;
import joshie.progression.crafting.Crafter;
import joshie.progression.crafting.CraftingRegistry;
import joshie.progression.gui.core.GuiCore;
import joshie.progression.gui.editors.GuiCriteriaEditor;
import joshie.progression.helpers.CraftingHelper;
import joshie.progression.helpers.MCClientHelper;
import joshie.progression.helpers.PlayerHelper;
import joshie.progression.helpers.SplitHelper;
import joshie.progression.lib.GuiIDs;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

import java.util.Set;
import java.util.concurrent.Callable;

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

        World world = event.player != null ? event.player.worldObj : event.tile != null ? event.tile.getWorld() : null;
        if (world != null) {
            if (!crafter.canUseItemWithAction(world, event.type, event.stack)) {
                event.setCanceled(true);
            }
        }
    }

    public static Cache<ItemStack, HashMultimap<ActionType, ICriteria>> tooltipCache = CacheBuilder.newBuilder().maximumSize(128).build();

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onItemTooltipEvent(final ItemTooltipEvent event) {
        if (event.itemStack == null || event.itemStack.getItem() == null) return;
        try {
            HashMultimap<ActionType, ICriteria> requirements = tooltipCache.get(event.itemStack, new Callable<HashMultimap<ActionType, ICriteria>>() {
                @Override
                public HashMultimap<ActionType, ICriteria> call() throws Exception {
                    HashMultimap<ActionType, ICriteria> criteria = HashMultimap.create();
                    for (ActionType type: ActionType.values()) {
                        Set<ICriteria> required = CraftingRegistry.getRequirements(type, event.itemStack);
                        if (required.size() == 0) continue;;
                        Set<ICriteria> completed = ProgressionAPI.player.getCompletedCriteriaList(PlayerHelper.getClientUUID(), true);
                        if (completed.contains(required)) continue; //Don't add this as a requirement if it's already completed
                        criteria.get(type).addAll(required);
                    }

                    return criteria;
                }
            });

           //Option 1
            if (requirements.size() >= 1) {
                event.toolTip.add(EnumChatFormatting.AQUA + "Actions Currently Locked");
                if (GuiScreen.isShiftKeyDown()) {
                    for (ActionType type : requirements.keySet()) {
                        event.toolTip.add(EnumChatFormatting.WHITE + "• " + type.getDisplayName());
                        event.toolTip.add(EnumChatFormatting.GOLD + "  " + EnumChatFormatting.ITALIC + "Criteria Required");
                        StringBuilder builder = new StringBuilder();
                        boolean first = true;
                        for (ICriteria criteria : requirements.get(type)) {
                            if (first) {
                                first = false;
                                builder.append("    " + criteria.getLocalisedName());
                            } else builder.append(", " + criteria.getLocalisedName());
                        }

                        String[] split = SplitHelper.splitStringEvery(builder.toString(), 45);
                        for (String s : split) {
                            event.toolTip.add(s);
                        }
                    }
                } else {
                    event.toolTip.add("Hold" + EnumChatFormatting.BLUE + " " + EnumChatFormatting.ITALIC + "Shift" + EnumChatFormatting.RESET + " to see details");
                    event.toolTip.add("Hold" + EnumChatFormatting.GOLD + " " + EnumChatFormatting.ITALIC + "Ctrl" + EnumChatFormatting.RESET + " to open criteria");
                    if (GuiScreen.isCtrlKeyDown()) {
                        for (ICriteria c: requirements.values()) {
                            MCClientHelper.FORCE_EDIT = false;
                            GuiCore.INSTANCE.setEditor(GuiCriteriaEditor.INSTANCE);
                            GuiCriteriaEditor.INSTANCE.setCriteria(c);
                            MCClientHelper.getPlayer().openGui(Progression.instance, GuiIDs.EDITOR, MCClientHelper.getWorld(), 0, 0, 0);
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
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
