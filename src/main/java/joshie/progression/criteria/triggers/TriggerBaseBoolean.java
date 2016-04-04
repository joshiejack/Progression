package joshie.progression.criteria.triggers;

import joshie.progression.api.special.IStoreTriggerData;
import net.minecraft.nbt.NBTTagCompound;

import java.util.UUID;

public abstract class TriggerBaseBoolean extends TriggerBase implements IStoreTriggerData {
    protected transient boolean value;

    @Override
    public boolean isCompleted() {
        return value;
    }

    @Override
    public boolean onFired(UUID uuid, Object... data) {
        value = isTrue(data);
        return true;
    }

    @Override
    public int getPercentage() {
        return value ? 100: 0;
    }

    @Override
    public void readDataFromNBT(NBTTagCompound tag) {
        value = tag.getBoolean("Value");
    }

    @Override
    public void writeDataToNBT(NBTTagCompound tag) {
        tag.setBoolean("Value", value);
    }

    //Helper Methods
    public TriggerBaseBoolean copyBoolean(TriggerBaseBoolean trigger) {
        trigger.value = value;
        return trigger;
    }

    protected boolean isTrue(Object... data) {
        return false;
    }

    protected void markTrue() {
        value = true;
    }
}