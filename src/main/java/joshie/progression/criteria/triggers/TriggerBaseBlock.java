package joshie.progression.criteria.triggers;

import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.criteria.IField;
import joshie.progression.api.criteria.IFilterProvider;
import joshie.progression.api.criteria.IFilterType;
import joshie.progression.api.special.DisplayMode;
import joshie.progression.api.special.ISpecialFieldProvider;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import java.util.List;

public abstract class TriggerBaseBlock extends TriggerBaseItemFilter implements ISpecialFieldProvider {
    @Override
    public void addSpecialFields(List<IField> fields, DisplayMode mode) {
        if (mode == DisplayMode.EDIT) fields.add(ProgressionAPI.fields.getItemPreview(this, "filters", 30, 35, 1.9F));
        else fields.add(ProgressionAPI.fields.getItemPreview(this, "filters", 65, 35, 1.9F));
    }

    @Override
    protected boolean canIncrease(Object... data) {
        Block theBlock = (Block) data[0];
        int theMeta = (Integer) data[1];
        for (IFilterProvider filter : filters) {
            if (filter.getProvided().matches(new ItemStack(theBlock, theMeta))) return true;
        }

        return false;
    }

    @Override
    public IFilterType getFilterForField(String fieldName) {
        return ProgressionAPI.filters.getBlockFilter();
    }
}
