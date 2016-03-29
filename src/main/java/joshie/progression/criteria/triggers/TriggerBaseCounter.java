package joshie.progression.criteria.triggers;

import joshie.progression.api.special.IAdditionalTooltip;
import joshie.progression.api.special.IStoreTriggerData;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;
import java.util.UUID;

public abstract class TriggerBaseCounter extends TriggerBase implements IStoreTriggerData, IAdditionalTooltip {
    public int amount = 1;
    protected transient int counter;

    public TriggerBaseCounter(String name, int color) {
        super(name, color, "count");
    }

    public TriggerBaseCounter(String name, int color, String data) {
        super(name, color, data);
    }
    
    public TriggerBaseCounter(ItemStack stack, String name, int color) {
        super(stack, name, color);
    }

    public TriggerBaseCounter copyCounter(TriggerBaseCounter trigger) {
        trigger.amount = amount;
        return trigger;
    }

    protected int getPercentage() {
        return (counter * 100) / amount;
    }

    @Override
    public boolean isCompleted() {
        return counter >= amount;
    }

    @Override
    public boolean onFired(UUID uuid, Object... data) {
        if (canIncrease(data) && counter < amount) {
            counter++;
        }

        return true;
    }

    protected boolean canIncrease(Object... data) {
        return canIncrease();
    }

    protected boolean canIncrease() {
        return false;
    }

    @Override
    public void addHoverTooltip(List<String> tooltip) {
        tooltip.add(counter + "/" + amount);
    }

    @Override
    public void readDataFromNBT(NBTTagCompound tag) {
        counter = tag.getInteger("Count");
    }

    @Override
    public void writeDataToNBT(NBTTagCompound tag) {
        tag.setInteger("Count", counter);
    }
}
