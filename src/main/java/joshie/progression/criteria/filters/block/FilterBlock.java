package joshie.progression.criteria.filters.block;

import java.util.List;

import joshie.progression.api.ISetterCallback;
import joshie.progression.api.fields.IField;
import joshie.progression.api.fields.IItemGetterCallback;
import joshie.progression.api.fields.ISpecialFieldProvider;
import joshie.progression.gui.fields.ItemField;
import joshie.progression.gui.newversion.overlays.FeatureItemSelector.Type;
import joshie.progression.gui.selector.filters.BlockFilter;
import joshie.progression.helpers.ItemHelper;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class FilterBlock extends FilterBaseBlock implements IItemGetterCallback, ISetterCallback, ISpecialFieldProvider {
    public Block filterBlock = Blocks.sandstone;

    public FilterBlock() {
        super("blockOnly", 0xFFCCCCCC);
    }

    @Override
    public boolean shouldReflectionSkipField(String name) {
        return true;
    }

    @Override
    public void addSpecialFields(List<IField> fields, DisplayMode mode) {
        if (mode == DisplayMode.EDIT) {
            fields.add(new ItemField("filterBlock", this, 25, 25, 3F, Type.TRIGGER, BlockFilter.INSTANCE));
        }
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
