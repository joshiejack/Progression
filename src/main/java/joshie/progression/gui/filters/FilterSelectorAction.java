package joshie.progression.gui.filters;

import java.util.ArrayList;
import java.util.List;

import joshie.progression.api.criteria.IProgressionFilterSelector;
import joshie.progression.api.criteria.IProgressionFilter.FilterType;
import joshie.progression.crafting.ActionType;
import net.minecraft.item.ItemStack;

public class FilterSelectorAction extends FilterSelectorItem {
    public static final IProgressionFilterSelector INSTANCE = new FilterSelectorAction();
    
    @Override
    public List<ItemStack> getAllItems() {
        List<ItemStack> list = new ArrayList();
        for (ActionType type: ActionType.values()) {
            list.add(type.getIcon());
        }
        
        return list;
    }

    @Override
    public FilterType getType() {
        return FilterType.CRAFTING;
    }

    @Override
    public boolean isAcceptedItem(ItemStack stack) {
        for (ActionType type: ActionType.values()) {
            if (type.getIcon().getItem() == stack.getItem() && type.getIcon().getItemDamage() == stack.getItemDamage()) return true;
        }
        
        return false;
    }
}
