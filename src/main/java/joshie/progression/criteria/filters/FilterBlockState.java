package joshie.progression.criteria.filters;

import net.minecraft.block.Block;

public class FilterBlockState extends FilterBaseBlock {
    public int blockState = 1;

    public FilterBlockState() {
        super("blockState", 0xFF663300);
    }

    @Override
    protected boolean matches(Block block, int meta) {
        return blockState == meta;
    }
}
