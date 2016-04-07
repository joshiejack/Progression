package joshie.progression.api.criteria;

import joshie.progression.api.special.IHasEventBus;
import net.minecraft.item.ItemStack;

public interface IAction {
    /** Set the representative item for this action **/
    public IAction setItemStack(ItemStack stack);

    /** Set the event handler for this, make sure the class is a singleton **/
    public IAction setEventHandler(IHasEventBus event);
}
