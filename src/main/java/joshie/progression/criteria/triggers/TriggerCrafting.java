package joshie.progression.criteria.triggers;

import joshie.progression.Progression;
import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.criteria.*;
import joshie.progression.api.special.DisplayMode;
import joshie.progression.api.special.ISpecialFieldProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;

import java.util.List;
import java.util.UUID;

@ProgressionRule(name="crafting", color=0xFF663300)
public class TriggerCrafting extends TriggerBaseItemFilter implements ISpecialFieldProvider {
    public int timesCrafted = 1;
    protected transient int timesItemCrafted;

    @Override
    public ITrigger copy() {
        TriggerCrafting trigger = new TriggerCrafting();
        trigger.timesCrafted = timesCrafted;
        return copyCounter(copyFilter(trigger));
    }

    @Override
    public String getDescription() {
        int percentageItemTotal = (counter * 100) / amount;
        int percentageCraftedTotal = (timesItemCrafted * 100) / timesCrafted;
        int percentageTotal = (percentageItemTotal + percentageCraftedTotal) / 2;
        return Progression.format("trigger.crafting.description", amount);
    }

    @Override
    public void addSpecialFields(List<IField> fields, DisplayMode mode) {
        if (mode == DisplayMode.EDIT) fields.add(ProgressionAPI.fields.getItemPreview(this, "filters", 30, 35, 1.9F));
        else fields.add(ProgressionAPI.fields.getItemPreview(this, "filters", 65, 35, 1.9F));
    }

    @SubscribeEvent
    public void onEvent(ItemCraftedEvent event) {
        ProgressionAPI.registry.fireTrigger(event.player, getProvider().getUnlocalisedName(), event.crafting.copy());
    }

    @Override
    public boolean isCompleted() {
        return counter >= amount && timesItemCrafted >= timesCrafted;
    }

    @Override
    public boolean onFired(UUID uuid, Object... additional) {
        ItemStack crafted = (ItemStack) (additional[0]);
        for (IFilterProvider filter : filters) {
            if (filter.getProvided().matches(crafted)) {
                counter += crafted.stackSize;
                timesItemCrafted++;
                return true;
            }
        }

        return true;
    }

    @Override
    public void readDataFromNBT(NBTTagCompound tag) {
        super.readDataFromNBT(tag);
        timesItemCrafted = tag.getInteger("Times");
    }

    @Override
    public void writeDataToNBT(NBTTagCompound tag) {
        super.writeDataToNBT(tag);
        tag.setInteger("Times", timesItemCrafted);
    }
}
