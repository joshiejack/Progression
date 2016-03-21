package joshie.progression.api.fields;

import net.minecraft.item.ItemStack;

/** Implement this on field providers,
 *  where their stack item isn't stored as a stack,
 *   so that you can can correctly return a stack; */
public interface IItemGetterCallback {
    
    /** Return the item for this field **/
    public ItemStack getItem(String fieldName);
}
