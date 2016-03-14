package joshie.progression.gui.newversion.overlays;

import net.minecraft.item.ItemStack;

public interface IItemSelectorFilter {
    /** Return the unlocalised name of this filter **/
    String getName();

    boolean isAcceptable(ItemStack stack);

}
