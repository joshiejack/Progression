package joshie.progression.criteria.filters.item;

import joshie.progression.api.criteria.ProgressionRule;
import joshie.progression.helpers.StackHelper;
import net.minecraft.item.ItemStack;

@ProgressionRule(name="modid", color=0xFFFF8000)
public class FilterItemMod extends FilterBaseItem {
    public String modid = "minecraft";

    @Override
    public boolean matches(ItemStack check) {
        if (modid.equals("*")) return true;
        return StackHelper.getModFromItem(check.getItem()).equals(modid);
    }
}
