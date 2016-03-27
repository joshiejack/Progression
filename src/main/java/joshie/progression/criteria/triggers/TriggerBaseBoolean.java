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

    protected boolean isTrue(Object... data) {
        return false;
    }

    protected void markTrue() {
        value = true;
    }

    @Override
    public void readDataFromNBT(NBTTagCompound tag) {
        tag.setBoolean("Value", value);
    }

    @Override
    public void writeDataToNBT(NBTTagCompound tag) {
        value = tag.getBoolean("Value");
    }
}