package joshie.progression.criteria.rewards;

import java.util.ArrayList;
import java.util.List;

import joshie.progression.api.IFilter;
import joshie.progression.api.fields.IHasFilters;
import joshie.progression.helpers.ItemHelper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public abstract class RewardBaseItemFilter extends RewardBase implements IHasFilters {
    public List<IFilter> filters = new ArrayList();
    protected ItemStack BROKEN;
    protected ItemStack preview;
    protected int ticker;

    public RewardBaseItemFilter(String name, int color) {
        super(name, color);
        BROKEN = new ItemStack(Items.baked_potato);
    }
    
    @Override
    public List<IFilter> getAllFilters() {
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
