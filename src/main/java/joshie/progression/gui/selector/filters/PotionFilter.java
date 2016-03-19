package joshie.progression.gui.selector.filters;

import java.util.List;

import joshie.progression.api.IFilter.FilterType;
import joshie.progression.api.filters.IFilterSelectorFilter;
import joshie.progression.helpers.ItemHelper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class PotionFilter extends ItemFilter {
    public static final IFilterSelectorFilter INSTANCE = new PotionFilter();
    
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
