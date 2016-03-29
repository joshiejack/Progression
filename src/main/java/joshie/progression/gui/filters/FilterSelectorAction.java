package joshie.progression.gui.filters;

import joshie.progression.api.criteria.IProgressionFilterSelector;
import joshie.progression.crafting.ActionType;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class FilterSelectorAction extends FilterSelectorItem {
    public static final IProgressionFilterSelector INSTANCE = new FilterSelectorAction();

    @Override
    public String getName() {
        return "crafting";
    }

    @Override
    public List<ItemStack> getAllItems() {
        List<ItemStack> list = new ArrayList();
        for (ActionType type: ActionType.values()) {
            list.add(type.getIcon());
        }
        
        return list;
    }

    @Override
    public boolean isAcceptedItem(ItemStack stack) {
        for (ActionType type: ActionType.values()) {
            if (type.getIcon().getItem() == stack.getItem() && type.getIcon().getItemDamage() == stack.getItemDamage()) return true;
        }
        
        return false;
    }
}
