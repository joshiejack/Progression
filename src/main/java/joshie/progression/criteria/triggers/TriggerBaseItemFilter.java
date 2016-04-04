package joshie.progression.criteria.triggers;

import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.criteria.IFilterProvider;
import joshie.progression.api.criteria.IFilterType;
import joshie.progression.api.special.ICustomIcon;
import joshie.progression.api.special.IHasFilters;
import joshie.progression.helpers.ItemHelper;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public abstract class TriggerBaseItemFilter extends TriggerBaseCounter implements IHasFilters, ICustomIcon {
    public List<IFilterProvider> filters = new ArrayList();
    protected ItemStack BROKEN;
    protected ItemStack preview;
    protected int ticker;

    public TriggerBaseItemFilter copyFilter(TriggerBaseItemFilter trigger) {
        trigger.filters = filters;
        return trigger;
    }
    
    @Override
    public List<IFilterProvider> getAllFilters() {
        return filters;
    }

    @Override
    public IFilterType getFilterForField(String fieldName) {
        return ProgressionAPI.filters.getItemStackFilter();
    }
    
    @Override
    public ItemStack getIcon() {
        if (ticker == 0 || ticker >= 200) {
            preview = ItemHelper.getRandomItem(filters);
            ticker = 1;
        }
        
        ticker++;
        
        return preview == null ? BROKEN: preview;
    }
}
