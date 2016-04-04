package joshie.progression.criteria.filters.action;

import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.criteria.IFilterType;
import joshie.progression.crafting.ActionType;
import joshie.progression.criteria.filters.FilterBase;
import net.minecraft.item.ItemStack;

public abstract class FilterBaseAction extends FilterBase {
    @Override
    public boolean matches(Object object) {
        if (!(object instanceof ItemStack)) return false;
        ItemStack stack = ((ItemStack) object);
        boolean accepted = false;
        for (ActionType type : ActionType.values()) {
            if ((type.getIcon().getItem() == stack.getItem() && type.getIcon().getItemDamage() == stack.getItemDamage())) {
                accepted = true;
                break;
            }
        }

        return accepted ? matches(stack) : false;
    }

    @Override
    public IFilterType getType() {
        return ProgressionAPI.filters.getActionFilter();
    }

    protected abstract boolean matches(ItemStack stack);
}
