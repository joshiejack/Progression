package joshie.progression.criteria.triggers;

import joshie.progression.api.IBlocksOnly;
import joshie.progression.api.ICancelable;
import joshie.progression.api.IItemFilter;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class TriggerBaseBlock extends TriggerBaseItemFilter implements ICancelable, IBlocksOnly {
    public boolean cancel = false;

    public TriggerBaseBlock(String unlocalised, int color) {
        super(unlocalised, color);
    }

    @Override
    protected boolean canIncrease(Object... data) {
        Block theBlock = (Block) data[0];
        int theMeta = (Integer) data[1];
        for (IItemFilter filter : filters) {
            if (filter.matches(new ItemStack(theBlock, theMeta))) return true;
        }

        return false;
    }

    @Override
    public boolean isCanceling() {
        return cancel;
    }

    @Override
    public void setCanceling(boolean isCanceled) {
        this.cancel = isCanceled;
    }
}
