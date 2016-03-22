package joshie.progression.gui.filters;

import joshie.progression.api.IFilterRegistry;
import joshie.progression.api.criteria.IProgressionFilterSelector;

public class FilterSelectorHelper implements IFilterRegistry {
    @Override
    public IProgressionFilterSelector getBlockFilter() {
        return FilterSelectorBlock.INSTANCE;
    }

    @Override
    public IProgressionFilterSelector getEntityFilter() {
        return FilterSelectorEntity.INSTANCE;
    }

    @Override
    public IProgressionFilterSelector getPotionFilter() {
        return FilterSelectorPotion.INSTANCE;
    }

    @Override
    public IProgressionFilterSelector getLocationFilter() {
        return FilterSelectorLocation.INSTANCE;
    }

    @Override
    public IProgressionFilterSelector getItemStackFilter() {
        return FilterSelectorItem.INSTANCE;
    }

    @Override
    public IProgressionFilterSelector getCraftingFilter() {
        return FilterSelectorAction.INSTANCE;
    }
}
