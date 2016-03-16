package joshie.progression.criteria.triggers;

import java.util.ArrayList;
import java.util.List;

import joshie.progression.api.IFilter;
import joshie.progression.helpers.ItemHelper;
import net.minecraft.item.ItemStack;

public abstract class TriggerBaseItemFilter extends TriggerBaseCounter {
    public List<IFilter> filters = new ArrayList();
    protected ItemStack BROKEN;
    protected ItemStack preview;
    protected int ticker;

    public TriggerBaseItemFilter(String name, int color) {
        super(name, color);
    }

    public TriggerBaseItemFilter(String name, int color, String data) {
        super(name, color, data);
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
