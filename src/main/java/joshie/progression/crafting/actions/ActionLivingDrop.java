package joshie.progression.crafting.actions;

import joshie.progression.crafting.ActionType;
import joshie.progression.handlers.ProgressionEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Iterator;

public class ActionLivingDrop extends ActionForgeEvent {
    public static final ActionLivingDrop INSTANCE = new ActionLivingDrop();

    @SubscribeEvent
    public void onLivingDrop(LivingDropsEvent event) {
        Entity source = event.getSource().getSourceOfDamage();
        if (source instanceof EntityPlayer) {
            Iterator<EntityItem> it = event.getDrops().iterator();
            while (it.hasNext()) {
                EntityItem item = it.next();
                ItemStack stack = item.getEntityItem();
                EntityPlayer player = (EntityPlayer) source;
                if (ProgressionEvents.isEventCancelled(player, ActionType.ENTITYDROPKILLEDWITH, player.getHeldItemMainhand(), ActionType.ENTITYDROP, stack)) {
                    it.remove();
                }
            }
        }
    }
}
