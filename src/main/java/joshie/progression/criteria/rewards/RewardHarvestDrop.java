package joshie.progression.criteria.rewards;

import java.util.Iterator;

import joshie.progression.crafting.ActionType;
import joshie.progression.handlers.CraftingEvents;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class RewardHarvestDrop extends RewardBaseAction {
    public RewardHarvestDrop() {
        super("harvestDrop", 0xFF555555);
        this.type = ActionType.HARVESTDROP;
    }

    @SubscribeEvent
    public void onHarvestDrop(HarvestDropsEvent event) {
        EntityPlayer player = event.harvester;
        if (player != null) {
            Iterator<ItemStack> it = event.drops.iterator();
            while (it.hasNext()) {
                ItemStack stack = it.next();
                if (CraftingEvents.isEventCancelled(player, ActionType.HARVESTDROP, player.getCurrentEquippedItem(), stack)) {
                    it.remove();
                }
            }
        }
    }
}
