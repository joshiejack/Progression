package joshie.progression.criteria.triggers;

import joshie.progression.Progression;
import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.criteria.IProgressionField;
import joshie.progression.api.criteria.IProgressionFilter;
import joshie.progression.api.criteria.IProgressionTrigger;
import joshie.progression.api.special.ISpecialFieldProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;

import java.util.List;
import java.util.UUID;

public class TriggerCrafting extends TriggerBaseItemFilter implements ISpecialFieldProvider {
    public int timesCrafted = 1;
    protected transient int amountItemCrafted;
    protected transient int timesItemCrafted;

    public TriggerCrafting() {
        super("crafting", 0xFF663300, "crafting");
    }

    @Override
    public IProgressionTrigger copy() {
        TriggerCrafting trigger = new TriggerCrafting();
        trigger.timesCrafted = timesCrafted;
        return copyBase(copyCounter(copyFilter(trigger)));
    }

    @SubscribeEvent
    public void onEvent(ItemCraftedEvent event) {
        ProgressionAPI.registry.fireTrigger(event.player, getUnlocalisedName(), event.crafting.copy());
    }

    @Override
    public void addSpecialFields(List<IProgressionField> fields, DisplayMode mode) {
        if (mode == DisplayMode.EDIT) fields.add(ProgressionAPI.fields.getItemPreview(this, "filters", 30, 35, 1.9F));
        else fields.add(ProgressionAPI.fields.getItemPreview(this, "filters", 65, 35, 1.9F));
    }

    @Override
    public boolean isCompleted() {
        return amountItemCrafted >= amount && timesItemCrafted >= timesCrafted;
    }

    @Override
    public boolean onFired(UUID uuid, Object... additional) {
        ItemStack crafted = (ItemStack) (additional[0]);
        for (IProgressionFilter filter : filters) {
            if (filter.matches(crafted)) {
                amountItemCrafted += crafted.stackSize;
                timesItemCrafted++;
                return true;
            }
        }

        return true;
    }

    @Override
    public String getDescription() {
        return Progression.format("trigger.crafting.description", amount) + "\n\n" + Progression.format("completed", getPercentage());
    }

    @Override
    public void readDataFromNBT(NBTTagCompound tag) {
        amountItemCrafted = tag.getInteger("Crafted");
        timesItemCrafted = tag.getInteger("Times");
    }

    @Override
    public void writeDataToNBT(NBTTagCompound tag) {
        tag.setInteger("Crafted", amountItemCrafted);
        tag.setInteger("Times", timesItemCrafted);
    }
}
