package joshie.progression.handlers;

import joshie.progression.Progression;
import joshie.progression.json.Options;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

/** Sync data, and make locked items useless **/
public class EventsHandler {
    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerLoggedInEvent event) {
        RemappingHandler.onPlayerConnect((EntityPlayerMP) event.player);
    }

    private boolean isBook(ItemStack stack) {
        if (Options.editor) {
            if (stack != null) {
                if (stack.getItem() == Items.book) {
                    return true;
                }
            }
        }

        return false;
    }

    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (isBook(event.entityPlayer.getCurrentEquippedItem())) {
            event.entityPlayer.openGui(Progression.instance, 0, null, 0, 0, 0);
        }
    }
    
    @SubscribeEvent
    public void onItemTooltipEvent(ItemTooltipEvent event) {
        if (isBook(event.itemStack) && event.entityPlayer.capabilities.isCreativeMode) {
            event.toolTip.add("Right click me to open");
            event.toolTip.add("'Progression editor'");
        }
    }
}
