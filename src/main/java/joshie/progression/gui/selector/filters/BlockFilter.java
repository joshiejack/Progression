package joshie.progression.gui.selector.filters;

import java.util.List;

import joshie.progression.api.IFilter.FilterType;
import joshie.progression.api.filters.IFilterSelectorFilter;
import joshie.progression.helpers.ItemHelper;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class BlockFilter extends ItemFilter {
    public static final IFilterSelectorFilter INSTANCE = new BlockFilter();
    
    @Override
    public List<ItemStack> getAllItems() {
        return ItemHelper.getAllItems();
    }
    
    @Override
    public FilterType getType() {
        return FilterType.BLOCK;
    }

    @Override
    public boolean isAcceptedItem(ItemStack stack) {
        Block block = null;
        int meta = 0;

        try {
            block = Block.getBlockFromItem(stack.getItem());
            meta = stack.getItemDamage();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return block != null;
    }
}
