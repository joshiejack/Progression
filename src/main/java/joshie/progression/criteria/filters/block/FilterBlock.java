package joshie.progression.criteria.filters.block;

import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.criteria.IProgressionField;
import joshie.progression.api.criteria.IProgressionFilterSelector;
import joshie.progression.api.special.*;
import joshie.progression.gui.filters.FilterSelectorBlock;
import joshie.progression.helpers.ItemHelper;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import java.util.List;

public class FilterBlock extends FilterBaseBlock implements IItemGetterCallback, ISetterCallback, ISpecialFilters, ISpecialFieldProvider {
    public Block filterBlock = Blocks.sandstone;

    public FilterBlock() {
        super("blockOnly", 0xFFCCCCCC);
    }

    @Override
    public IProgressionFilterSelector getFilterForField(String fieldName) {
        return FilterSelectorBlock.INSTANCE;
    }

    @Override
    public void addSpecialFields(List<IProgressionField> fields, DisplayMode mode) {
        if (mode == DisplayMode.EDIT) fields.add(ProgressionAPI.fields.getItem(this, "filterBlock", 25, 25, 3F));
    }

    @Override
    protected boolean matches(Block block, int meta) {
        return filterBlock == block;
    }

    @Override
    public ItemStack getItem(String fieldName) {
        return new ItemStack(filterBlock);
    }

    @Override
    public boolean setField(String fieldName, Object stack) {
        try {
            filterBlock = ItemHelper.getBlock(((ItemStack) stack));
        } catch (Exception e) {}

        return true;
    }
}
