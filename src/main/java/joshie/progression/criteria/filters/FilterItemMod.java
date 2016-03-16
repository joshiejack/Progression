package joshie.progression.criteria.filters;

import joshie.progression.helpers.StackHelper;
import net.minecraft.item.ItemStack;

public class FilterItemMod extends FilterBaseItem {
    public String modid = "minecraft";

    public FilterItemMod() {
        super("modid", 0xFFFF8000);
    }

    @Override
    public boolean matches(ItemStack check) {
        return StackHelper.getModFromItem(check.getItem()).equals(modid);
    }
}
