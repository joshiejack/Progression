package joshie.progression.criteria.triggers;

import joshie.progression.Progression;
import joshie.progression.api.special.ICustomDescription;
import joshie.progression.api.special.IStoreTriggerData;
import net.minecraft.nbt.NBTTagCompound;

import java.util.UUID;

public abstract class TriggerBaseCounter extends TriggerBase implements ICustomDescription, IStoreTriggerData {
    public int amount = 1;
    protected transient int counter;

    public TriggerBaseCounter copyCounter(TriggerBaseCounter trigger) {
        trigger.amount = amount;
        return trigger;
    }

    @Override
    public String getDescription() {
        return Progression.format(getProvider().getUnlocalisedName() + ".description", amount);
    }

    @Override
    public int getPercentage() {
        System.out.println("Counter: " + counter + " " + this);
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

    @Override
    public void readDataFromNBT(NBTTagCompound tag) {
        counter = tag.getInteger("Count");
        System.out.println("Counter: " + counter + " " + this);
    }

    @Override
    public void writeDataToNBT(NBTTagCompound tag) {
        tag.setInteger("Count", counter);
    }

    //Helper Methods
    protected boolean canIncrease(Object... data) {
        return canIncrease();
    }

    protected boolean canIncrease() {
        return false;
    }
}
