package joshie.progression.gui.newversion.overlays;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;

public interface IItemSelectorFilter {
    /** Return the unlocalised name of this filter **/
    public String getName();

    /** Whether this item is acceptable **/
    public boolean isAcceptable(ItemStack stack);

    /** Add additional items 
     * @param list **/
    public void addExtraItems(ArrayList<ItemStack> list);
}
