package joshie.progression.criteria.triggers;

import java.util.UUID;

import joshie.progression.api.IItemFilter;
import joshie.progression.api.ITriggerData;
import joshie.progression.api.ProgressionAPI;
import joshie.progression.criteria.triggers.data.DataCrafting;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;

public class TriggerCrafting extends TriggerBaseItemFilter {
    public int times = 1;
    public int amount = 1;

    public TriggerCrafting() {
        super("crafting", 0xFF663300, "crafting");
    }

    @SubscribeEvent
    public void onEvent(ItemCraftedEvent event) {
        ProgressionAPI.registry.fireTrigger(event.player, getUnlocalisedName(), event.crafting.copy());
    }

    @Override
    public boolean isCompleted(ITriggerData existing) {
        DataCrafting data = (DataCrafting) existing;
        return data.amountCrafted >= amount && data.timesCrafted >= times;
    }

    @Override
    public boolean onFired(UUID uuid, ITriggerData existing, Object... additional) {
        DataCrafting data = (DataCrafting) existing;
        ItemStack crafted = (ItemStack) (additional[0]);
        for (IItemFilter filter : filters) {
            if (filter.matches(crafted)) {
                data.amountCrafted += crafted.stackSize;
                data.timesCrafted++;
                return true;
            }
        }

        return true;
    }
}
