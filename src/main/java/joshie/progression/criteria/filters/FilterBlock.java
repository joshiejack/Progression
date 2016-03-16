package joshie.progression.criteria.filters;

import java.util.List;

import joshie.progression.api.IField;
import joshie.progression.api.IItemGetterCallback;
import joshie.progression.api.ISetterCallback;
import joshie.progression.api.gui.ISpecialFieldProvider;
import joshie.progression.gui.fields.ItemField;
import joshie.progression.gui.newversion.overlays.FeatureItemSelector.Type;
import joshie.progression.gui.selector.filters.BlockFilter;
import joshie.progression.helpers.ItemHelper;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class FilterBlock extends FilterBase implements IItemGetterCallback, ISetterCallback, ISpecialFieldProvider {
    public Block block = Blocks.sandstone;

    public FilterBlock() {
        super("blockOnly", 0xFFCCCCCC);
    }

    @Override
    public void addSpecialFields(List<IField> fields) {
        fields.add(new ItemField("block", this, 25, 25, 3F, 26, 70, 25, 75, Type.TRIGGER, BlockFilter.INSTANCE));
    }

    @Override
    public boolean matches(ItemStack check) {
        return ItemHelper.getBlock(check) == block;
    }

    @Override
    public ItemStack getItem(String fieldName) {
        return new ItemStack(block);
    }

    @Override
    public boolean setField(String fieldName, Object stack) {
        block = ItemHelper.getBlock(((ItemStack) stack));

        return true;
    }
}
