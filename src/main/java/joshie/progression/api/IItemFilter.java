package joshie.progression.api;

import joshie.progression.lib.SafeStack;
import net.minecraft.item.ItemStack;

public interface IItemFilter extends IFilter {
    /** Return true if the pass in stack, matches this item filter. */
    public boolean matches(ItemStack stack);
}
