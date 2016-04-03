package joshie.progression.criteria.rewards;

import joshie.progression.api.criteria.IFilter;
import joshie.progression.api.special.IHasFilters;
import joshie.progression.helpers.ItemHelper;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public abstract class RewardBaseItemFilter extends RewardBase implements IHasFilters {
    public List<IFilter> filters = new ArrayList();
    protected ItemStack BROKEN;
    protected ItemStack preview;
    protected int ticker;

    public RewardBaseItemFilter(String name, int color) {
        super(name, color);
        BROKEN = new ItemStack(Items.baked_potato);
    }

    public RewardBaseItemFilter(ItemStack stack, String name, int color) {
        super(name, color);
        BROKEN = stack;
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

        if (!GuiScreen.isShiftKeyDown()) ticker++;
        
        return preview == null ? BROKEN: preview;
    }
}
