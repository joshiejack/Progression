package joshie.progression.criteria.filters.crafting;

import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.criteria.IProgressionFilterSelector;
import joshie.progression.crafting.ActionType;
import joshie.progression.criteria.filters.FilterBase;
import joshie.progression.helpers.ItemHelper;
import net.minecraft.item.ItemStack;

import java.util.List;

public abstract class FilterBaseCrafting extends FilterBase {
    public FilterBaseCrafting(String string, int color) {
        super(string, color);
    }

    @Override
    public List<ItemStack> getMatches(Object object) {
        return getMatches();
    }

    public List<ItemStack> getMatches() {
        return ItemHelper.getAllMatchingItems(this);
    }

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
    public IProgressionFilterSelector getType() {
        return ProgressionAPI.filters.getCraftingFilter();
    }

    protected abstract boolean matches(ItemStack stack);
}
