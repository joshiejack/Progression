package joshie.progression.handlers;

import com.google.common.collect.HashMultimap;
import joshie.progression.Progression;
import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.criteria.ICriteria;
import joshie.progression.api.event.ActionEvent;
import joshie.progression.crafting.ActionType;
import joshie.progression.crafting.Crafter;
import joshie.progression.crafting.CraftingRegistry;
import joshie.progression.gui.core.GuiCore;
import joshie.progression.helpers.*;
import joshie.progression.lib.GuiIDs;
import joshie.progression.network.PacketHandler;
import joshie.progression.network.PacketSyncUsernameCache;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.event.GuiScreenEvent.DrawScreenEvent;
import net.minecraftforge.common.UsernameCache;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Set;

import static joshie.progression.gui.core.GuiList.*;

public class ProgressionEvents {
    public static Checker checker = new Checker();
    public static class Checker {
        public boolean isRunnable(GuiScreen screen) {
            return screen instanceof GuiCore;
        }
    }

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerLoggedInEvent event) {
        RemappingHandler.onPlayerConnect((EntityPlayerMP) event.player);
        //Send to everybody
        PacketHandler.sendToEveryone(new PacketSyncUsernameCache(UsernameCache.getMap()));
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onGuiPost(DrawScreenEvent.Pre event) {
        if (checker.isRunnable(event.getGui())) {
            LAST.clear();
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onGuiPost(DrawScreenEvent.Post event) {
        if (checker.isRunnable(event.getGui())) {
            LAST.run();
        }
    }
    
    @SubscribeEvent
    public void onAttemptToObtainItem(ActionEvent event) {
        if (event.stack == null) return;
        if (event.world != null) {
            World world = event.world;
            Crafter crafter = event.player != null ? CraftingRegistry.get(world.isRemote).getCrafterFromPlayer(event.player) : CraftingRegistry.get(world.isRemote).getCrafterFromTile(event.tile);
            if (crafter.canDoAnything()) return;
            if (!crafter.canUseItemWithAction(world, event.type, event.stack)) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onItemTooltipEvent(final ItemTooltipEvent event) {
        if (event.getItemStack() == null || event.getItemStack().getItem() == null || event.getEntityPlayer() == null || CraftingRegistry.get(event.getEntityPlayer().worldObj.isRemote) == null) return;
        try {
            //No real way to cache correctly, without creating tons of objects
            HashMultimap<ActionType, ICriteria> requirements = HashMultimap.create();
            for (ActionType type: ActionType.values()) {
                Set<ICriteria> required = CraftingRegistry.get(event.getEntityPlayer().worldObj.isRemote).getRequirements(type, event.getItemStack());
                if (required.size() == 0) continue;;
                Set<ICriteria> completed = ProgressionAPI.player.getCompletedCriteriaList(PlayerHelper.getClientUUID(), true);
                if (completed.contains(required)) continue; //Don't add this as a requirement if it's already completed
                requirements.get(type).addAll(required);
            }

           //Option 1
            if (requirements.size() >= 1) {
                event.getToolTip().add(TextFormatting.AQUA + "Actions Currently Locked");
                if (GuiScreen.isShiftKeyDown()) {
                    for (ActionType type : requirements.keySet()) {
                        event.getToolTip().add(TextFormatting.WHITE + "• " + type.getDisplayName());
                        event.getToolTip().add(TextFormatting.GOLD + "  " + TextFormatting.ITALIC + "Criteria Required");
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
                            event.getToolTip().add(s);
                        }
                    }
                } else {
                    event.getToolTip().add("Hold" + TextFormatting.BLUE + " " + TextFormatting.ITALIC + "Shift" + TextFormatting.RESET + " to see details");
                    event.getToolTip().add("Hold" + TextFormatting.GOLD + " " + TextFormatting.ITALIC + "Ctrl" + TextFormatting.RESET + " to open criteria");
                    if (GuiScreen.isCtrlKeyDown()) {
                        for (ICriteria c: requirements.values()) {
                            MCClientHelper.FORCE_EDIT = false;
                            CORE.setEditor(CRITERIA_EDITOR);
                            CRITERIA_EDITOR.set(c);
                            event.getEntityPlayer().openGui(Progression.instance, GuiIDs.EDITOR, event.getEntityPlayer().worldObj, 0, 0, 0);
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
