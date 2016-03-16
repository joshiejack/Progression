package joshie.progression.criteria.filters;

import joshie.progression.helpers.ItemHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

public abstract class FilterBaseBlock extends FilterBase {
    public int meta = 0;

    public FilterBaseBlock(String string, int color) {
        super(string, color);
    }

    @Override
    public boolean matches(ItemStack check) {
        Block block = ItemHelper.getBlock(check);
        return block == null ? false : matches(block, block.getMetaFromState(block.getStateFromMeta(check.getItemDamage())));
    }

    protected abstract boolean matches(Block block, int metadata);
}
