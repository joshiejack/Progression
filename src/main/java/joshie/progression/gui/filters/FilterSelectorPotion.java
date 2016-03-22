package joshie.progression.gui.filters;

import java.util.List;

import joshie.progression.api.criteria.IProgressionFilterSelector;
import joshie.progression.api.criteria.IProgressionFilter.FilterType;
import joshie.progression.helpers.ItemHelper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class FilterSelectorPotion extends FilterSelectorItem {
    public static final IProgressionFilterSelector INSTANCE = new FilterSelectorPotion();
    
    @Override
    public List<ItemStack> getAllItems() {
        return ItemHelper.getAllItems();
    }

    @Override
    public FilterType getType() {
        return FilterType.POTIONEFFECT;
    }

    @Override
    public boolean isAcceptedItem(ItemStack stack) {
        return stack.getItem() == Items.potionitem && stack.getItemDamage() != 0;
    }
}
