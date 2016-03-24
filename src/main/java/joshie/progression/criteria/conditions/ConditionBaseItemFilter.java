package joshie.progression.criteria.conditions;

import joshie.progression.api.criteria.IProgressionFilter;
import joshie.progression.api.special.IHasFilters;
import joshie.progression.helpers.ItemHelper;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public abstract class ConditionBaseItemFilter extends ConditionBase implements IHasFilters {
    public List<IProgressionFilter> filters = new ArrayList();
    protected ItemStack BROKEN;
    protected ItemStack preview;
    protected int ticker;

    public ConditionBaseItemFilter(String name, int color) {
        super(name, color);
    }

    @Override
    public List<IProgressionFilter> getAllFilters() {
        return filters;
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
