package joshie.progression.criteria.triggers;

import java.util.List;

import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.criteria.IProgressionField;
import joshie.progression.api.criteria.IProgressionFilter;
import joshie.progression.api.criteria.IProgressionFilterSelector;
import joshie.progression.api.special.ICancelable;
import joshie.progression.api.special.ISpecialFieldProvider;
import joshie.progression.api.special.ISpecialFilters;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class TriggerBaseBlock extends TriggerBaseItemFilter implements ICancelable, ISpecialFilters, ISpecialFieldProvider {
    public boolean cancel = false;
    
    public TriggerBaseBlock(String unlocalised, int color) {
        super(unlocalised, color);
    }
    
    @Override
    public boolean shouldReflectionSkipField(String name) {
        return name.equals("filters");
    }
    
    @Override
    public void addSpecialFields(List<IProgressionField> fields, DisplayMode mode) {
        if (mode == DisplayMode.EDIT) fields.add(ProgressionAPI.fields.getItemPreview(this, "filters", 30, 35, 1.9F));
    }

    @Override
    protected boolean canIncrease(Object... data) {
        Block theBlock = (Block) data[0];
        int theMeta = (Integer) data[1];
        for (IProgressionFilter filter : filters) {
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
    public IProgressionFilterSelector getFilterForField(String fieldName) {
        return ProgressionAPI.filters.getBlockFilter();
    }
}
