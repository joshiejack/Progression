package joshie.progression.criteria.triggers;

import joshie.progression.api.ICancelable;
import joshie.progression.api.IFilter;
import joshie.progression.api.ISpecialFilters;
import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.filters.IFilterSelectorFilter;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class TriggerBaseBlock extends TriggerBaseItemFilter implements ICancelable, ISpecialFilters {
    public boolean cancel = false;
    
    public TriggerBaseBlock(String unlocalised, int color) {
        super(unlocalised, color);
    }

    @Override
    protected boolean canIncrease(Object... data) {
        Block theBlock = (Block) data[0];
        int theMeta = (Integer) data[1];
        for (IFilter filter : filters) {
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
    
    @Override
    public IFilterSelectorFilter getFilterForField(String fieldName) {
        return ProgressionAPI.filters.getBlockFilter();
    }
}
