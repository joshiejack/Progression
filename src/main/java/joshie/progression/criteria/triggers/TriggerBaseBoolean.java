package joshie.progression.criteria.triggers;

import joshie.progression.api.special.IStoreTriggerData;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.UUID;

public abstract class TriggerBaseBoolean extends TriggerBase implements IStoreTriggerData {
    protected transient boolean value;

    public TriggerBaseBoolean(ItemStack stack, String name, int color) {
        super(stack, name, color);
    }

    @Override
    public boolean isCompleted() {
        return value;
    }

    @Override
    public boolean onFired(UUID uuid, Object... data) {
        value = isTrue(data);
        return true;
    }

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
}