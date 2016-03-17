package joshie.progression.criteria.filters.block;

import joshie.progression.criteria.filters.item.FilterItemOre;
import joshie.progression.helpers.ItemHelper;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class FilterBlockOre extends FilterItemOre {
    public FilterBlockOre() {
        super("blockOre", 0xFF663300);
    }

    @Override
    public boolean matches(ItemStack check) {
        Block block = ItemHelper.getBlock(check);
        return block == null ? false : super.matches(check);
    }
    
    @Override
    public FilterType getType() {
        return FilterType.BLOCK;
    }
}
