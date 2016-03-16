package joshie.progression.api;

import net.minecraft.item.ItemStack;

public interface IFilter extends IFieldProvider {
    /** Return true if the pass in object, matches this filter.
     *  Keep in mind this can pass in entities, itemstack,
     *  Lists or anything really, so make sure to validate
     *  all objects */
    public boolean matches(Object onject);
}
