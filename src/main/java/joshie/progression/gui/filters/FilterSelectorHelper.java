package joshie.progression.gui.filters;

import joshie.progression.api.IFilterRegistry;
import joshie.progression.api.criteria.IFilterType;

public class FilterSelectorHelper implements IFilterRegistry {
    @Override
    public IFilterType getBlockFilter() {
        return FilterTypeBlock.INSTANCE;
    }

    @Override
    public IFilterType getEntityFilter() {
        return FilterTypeEntity.INSTANCE;
    }

    @Override
    public IFilterType getPotionFilter() {
        return FilterTypePotion.INSTANCE;
    }

    @Override
    public IFilterType getLocationFilter() {
        return FilterTypeLocation.INSTANCE;
    }

    @Override
    public IFilterType getItemStackFilter() {
        return FilterTypeItem.INSTANCE;
    }

    @Override
    public IFilterType getActionFilter() {
        return FilterTypeAction.INSTANCE;
    }
}
