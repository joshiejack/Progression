package joshie.progression.criteria.filters.item;

import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.criteria.IFilter;
import joshie.progression.api.criteria.IFilterProvider;
import joshie.progression.api.criteria.IFilterType;
import joshie.progression.helpers.ItemHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public abstract class FilterBaseItem implements IFilter<ItemStack> {
    private IFilterProvider provider;

    @Override
    public IFilterProvider getProvider() {
        return provider;
    }

    @Override
    public void setProvider(IFilterProvider provider) {
        this.provider = provider;
    }

    @Override
    public void apply(ItemStack stack) {}

    @Override
    public ItemStack getRandom(EntityPlayer player) {
        return ItemHelper.getRandomItem(this.getProvider());
    }
    
    @Override
    public boolean matches(Object object) {
        return object instanceof ItemStack ? matches((ItemStack)object) : false;
    }
    
    @Override
    public IFilterType getType() {
        return ProgressionAPI.filters.getItemStackFilter();
    }
    
    public abstract boolean matches(ItemStack stack);
}
