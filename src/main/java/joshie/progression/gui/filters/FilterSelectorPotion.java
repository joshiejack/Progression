package joshie.progression.gui.filters;

import joshie.progression.api.criteria.IProgressionFilterSelector;
import joshie.progression.helpers.ItemHelper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.util.List;

public class FilterSelectorPotion extends FilterSelectorItem {
    public static final IProgressionFilterSelector INSTANCE = new FilterSelectorPotion();

    @Override
    public String getName() {
        return "potioneffect";
    }
    
    @Override
    public List<ItemStack> getAllItems() {
        return ItemHelper.getAllItems();
    }

    @Override
    public boolean isAcceptedItem(ItemStack stack) {
        return stack.getItem() == Items.potionitem && stack.getItemDamage() != 0;
    }
}
