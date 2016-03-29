package joshie.progression.gui.filters;

import joshie.progression.api.criteria.IProgressionFilterSelector;
import joshie.progression.helpers.ItemHelper;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import java.util.List;

public class FilterSelectorBlock extends FilterSelectorItem {
    public static final IProgressionFilterSelector INSTANCE = new FilterSelectorBlock();

    @Override
    public String getName() {
        return "block";
    }

    @Override
    public List<ItemStack> getAllItems() {
        return ItemHelper.getAllItems();
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
