package joshie.progression.criteria.filters.item;

import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.criteria.IProgressionFilterSelector;
import joshie.progression.criteria.filters.FilterBase;
import joshie.progression.helpers.ItemHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public abstract class FilterBaseItem extends FilterBase {
    public FilterBaseItem(String name, int color) {
        super(name, color);
    }

    @Override
    public ItemStack getRandom(EntityPlayer player) {
        return ItemHelper.getRandomItem(this);
    }
    
    @Override
    public boolean matches(Object object) {
        return object instanceof ItemStack ? matches((ItemStack)object) : false;
    }
    
    @Override
    public IProgressionFilterSelector getType() {
        return ProgressionAPI.filters.getItemStackFilter();
    }
    
    public abstract boolean matches(ItemStack stack);
}
