package joshie.progression.gui.fields;

import net.minecraft.item.ItemStack;

public interface IItemSetterCallback {
    public void setItem(String fieldName, ItemStack stack);
}
