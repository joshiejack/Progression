package joshie.progression.gui.filters;

import java.util.List;

import joshie.progression.api.criteria.IProgressionFilterSelector;
import joshie.progression.api.criteria.IProgressionFilter.FilterType;
import joshie.progression.helpers.ItemHelper;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class FilterSelectorBlock extends FilterSelectorItem {
    public static final IProgressionFilterSelector INSTANCE = new FilterSelectorBlock();
    
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
