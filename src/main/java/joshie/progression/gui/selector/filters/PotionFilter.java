package joshie.progression.gui.selector.filters;

import java.util.ArrayList;

import joshie.progression.gui.newversion.overlays.IItemSelectorFilter;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class PotionFilter implements IItemSelectorFilter {
    public static final IItemSelectorFilter INSTANCE = new PotionFilter();

    @Override
    public String getName() {
        return "potion";
    }

    @Override
    public boolean isAcceptable(ItemStack stack) {
        return stack.getItem() == Items.potionitem && stack.getItemDamage() != 0;
    }

    @Override
    public void addExtraItems(ArrayList<ItemStack> list) {
        for (int i = 1; i < Short.MAX_VALUE; i++) {
            //list.add(new ItemStack(Items.potionitem, 1, i));
        }
    }
}
