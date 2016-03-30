package joshie.progression.crafting;

import net.minecraft.item.ItemStack;

public abstract class Crafter {
    /** This is called to check whether the item is permitted to be crafted
     *  
     * @param           the stack
     * @return          whether it can perform this action or not */
    public abstract boolean canUseItemWithAction(ActionType type, ItemStack stack);

    /** This is used to bypass the canUseItemForCrafting check
     *  Which is handy if you don't want to check for materials
     *  
     * @return          returns true if you can craft anything */
    public abstract boolean canDoAnything();
}
