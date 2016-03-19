package joshie.progression.criteria.filters.block;

import java.util.List;

import joshie.progression.criteria.filters.FilterBase;
import joshie.progression.helpers.ItemHelper;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public abstract class FilterBaseBlock extends FilterBase {
    public FilterBaseBlock(String string, int color) {
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
        ItemStack check = (ItemStack) object;
        Block block = ItemHelper.getBlock(check);
        int meta = 0;
        
        try {
            meta = block.getMetaFromState(block.getStateFromMeta(check.getItemDamage()));
        } catch (Exception e) { return false; }
        
        return block == null ? false : matches(block, meta);
    }

    @Override
    public FilterType getType() {
        return FilterType.BLOCK;
    }

    protected abstract boolean matches(Block block, int metadata);
}
