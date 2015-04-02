package joshie.crafting;

import joshie.crafting.helpers.PlayerHelper;
import joshie.crafting.json.Options;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ServerConnectionFromClientEvent;

/** Sync data, and make locked items useless **/
public class CraftingEventsHandler {
    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerLoggedInEvent event) {
        CraftingRemapper.onPlayerConnect((EntityPlayerMP) event.player);
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
            event.entityPlayer.openGui(CraftingMod.instance, 0, null, 0, 0, 0);
        }
    }
}
