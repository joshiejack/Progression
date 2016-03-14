package joshie.progression.gui.selector.filters;

import joshie.progression.gui.newversion.overlays.IItemSelectorFilter;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class BlockFilter implements IItemSelectorFilter {
    @Override
    public String getName() {
        return "block";
    }

    @Override
    public boolean isAcceptable(ItemStack stack) {
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
