package joshie.progression.criteria.rewards;

import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.criteria.IFilterProvider;
import joshie.progression.api.criteria.IFilterType;
import joshie.progression.api.special.ICustomIcon;
import joshie.progression.api.special.IHasFilters;
import joshie.progression.helpers.ItemHelper;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public abstract class RewardBaseItemFilter extends RewardBase implements IHasFilters, ICustomIcon {
    public List<IFilterProvider> filters = new ArrayList();
    protected ItemStack BROKEN;
    protected ItemStack preview;
    protected int ticker;

    public RewardBaseItemFilter() {
        BROKEN = new ItemStack(Items.baked_potato);
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

        if (!GuiScreen.isShiftKeyDown()) ticker++;
        return preview == null ? BROKEN: preview;
    }
}
