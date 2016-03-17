package joshie.progression.criteria.filters.block;

import joshie.progression.helpers.StackHelper;
import net.minecraft.block.Block;

public class FilterBlockMod extends FilterBaseBlock {
    public String modid = "minecraft";

    public FilterBlockMod() {
        super("blockMod", 0xFF663300);
    }

    @Override
    protected boolean matches(Block block, int meta) {
        return StackHelper.getModFromBlock(block).equals(modid);
    }
}
