package joshie.progression.criteria.triggers;

import joshie.progression.api.special.IStoreTriggerData;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.UUID;

public abstract class TriggerBaseCounter extends TriggerBase implements IStoreTriggerData {
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

    @Override
    public boolean isCompleted() {
        return counter >= amount;
    }

    @Override
    public boolean onFired(UUID uuid, Object... data) {
        if (canIncrease(data)) {
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
    public void readDataFromNBT(NBTTagCompound tag) {
        tag.setInteger("Count", counter);
    }

    @Override
    public void writeDataToNBT(NBTTagCompound tag) {
        counter = tag.getInteger("Count");
    }
}
