package joshie.progression.criteria.filters.action;

import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.criteria.IFilter;
import joshie.progression.api.criteria.IFilterProvider;
import joshie.progression.api.criteria.IFilterType;
import joshie.progression.api.special.IAdditionalTooltip;
import joshie.progression.crafting.ActionType;
import net.minecraft.item.ItemStack;

import java.util.List;

public abstract class FilterBaseAction implements IFilter<ItemStack, ItemStack>, IAdditionalTooltip<ItemStack> {
    private IFilterProvider provider;

    @Override
    public IFilterProvider getProvider() {
        return provider;
    }

    @Override
    public void setProvider(IFilterProvider provider) {
        this.provider = provider;
    }

    @Override
    public void apply(ItemStack stack) {}

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

    @Override
    public void addHoverTooltip(String field, ItemStack stack, List<String> tooltip) {
        tooltip.clear(); //How dare you try to display the itemstacks tooltip!
        tooltip.add(ActionType.getCraftingActionFromIcon(stack).getDisplayName());
    }

    protected abstract boolean matches(ItemStack stack);
}