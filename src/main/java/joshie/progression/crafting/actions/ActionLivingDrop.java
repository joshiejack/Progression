package joshie.progression.crafting.actions;

import java.util.Iterator;

import joshie.progression.crafting.ActionType;
import joshie.progression.handlers.CraftingEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ActionLivingDrop extends ActionForgeEvent {
    public ActionLivingDrop(String name) {
        super(name);
    }

    @SubscribeEvent
    public void onLivingDrop(LivingDropsEvent event) {
        Entity source = event.source.getSourceOfDamage();
        if (source instanceof EntityPlayer) {
            Iterator<EntityItem> it = event.drops.iterator();
            while (it.hasNext()) {
                EntityItem item = it.next();
                ItemStack stack = item.getEntityItem();
                EntityPlayer player = (EntityPlayer) source;
                if (CraftingEvents.isEventCancelled(player, ActionType.ENTITYDROP, player.getCurrentEquippedItem(), stack)) {
                    it.remove();
                }
            }
        }
    }
}