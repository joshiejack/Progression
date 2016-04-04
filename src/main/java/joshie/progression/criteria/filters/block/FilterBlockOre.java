package joshie.progression.criteria.filters.block;

import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.criteria.IFilterType;
import joshie.progression.api.criteria.ProgressionRule;
import joshie.progression.criteria.filters.item.FilterItemOre;
import joshie.progression.helpers.ItemHelper;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

@ProgressionRule(name="blockOre", color=0xFF663300)
public class FilterBlockOre extends FilterItemOre {
    @Override
    public boolean matches(ItemStack check) {
        Block block = ItemHelper.getBlock(check);
        return block == null ? false : super.matches(check);
    }

    @Override
    public IFilterType getType() {
        return ProgressionAPI.filters.getBlockFilter();
    }
}
