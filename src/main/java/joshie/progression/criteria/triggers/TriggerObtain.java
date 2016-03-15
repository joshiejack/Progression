package joshie.progression.criteria.triggers;

import java.util.UUID;

import joshie.progression.api.IItemFilter;
import joshie.progression.api.ITriggerData;
import joshie.progression.api.ProgressionAPI;
import joshie.progression.criteria.triggers.data.DataCrafting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerOpenContainerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TriggerObtain extends TriggerBaseItemFilter {
    public int itemAmount = 1;
    public boolean consume = false;

    public TriggerObtain() {
        super("obtain", 0xFFFFFF00, "crafting");
    }

    private boolean fired = false;

    @SubscribeEvent
    public void onEvent(PlayerOpenContainerEvent event) {
        long time = event.entityPlayer.worldObj.getTotalWorldTime();
        if (time % 30 == 0) {
            if (fired) {
                for (int i = 0; i < event.entityPlayer.inventory.mainInventory.length; i++) {
                    ItemStack stack = event.entityPlayer.inventory.mainInventory[i];
                    if (stack == null) continue;
                    ProgressionAPI.registry.fireTrigger(event.entityPlayer, getUnlocalisedName(), stack, event.entityPlayer, i);
                }
            }

            fired = !fired;
        }
    }

    @Override
    public boolean isCompleted(ITriggerData existing) {
        DataCrafting data = (DataCrafting) existing;
        return data.amountCrafted >= itemAmount;
    }

    @Override
    public boolean onFired(UUID uuid, ITriggerData existing, Object... additional) {
        DataCrafting data = (DataCrafting) existing;
        ItemStack crafted = (ItemStack) additional[0];
        if (filters == null || crafted == null) return true;
        for (IItemFilter filter : filters) {
            if (filter.matches(crafted)) {
                data.amountCrafted += crafted.stackSize;
                data.timesCrafted++;

                if (consume) {
                    EntityPlayer player = (EntityPlayer) additional[1];
                    int slot = (Integer) additional[2];
                    player.inventory.decrStackSize(slot, crafted.stackSize);
                }

                return true;
            }
        }

        return true;
    }
}
