package joshie.progression.criteria.filters.block;

import java.util.List;

import joshie.progression.api.fields.IField;
import joshie.progression.api.fields.IInit;
import joshie.progression.api.fields.ISpecialFieldProvider;
import joshie.progression.gui.fields.ItemField;
import joshie.progression.gui.newversion.overlays.FeatureItemSelector.Type;
import joshie.progression.gui.selector.filters.BlockFilter;
import joshie.progression.helpers.ItemHelper;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class FilterBlockStack extends FilterBaseBlock implements ISpecialFieldProvider, IInit {
    public ItemStack stack = new ItemStack(Blocks.anvil);
    public boolean matchState = true;
    private Block filterBlock = Blocks.anvil;
    private int filterMeta = 0;

    public FilterBlockStack() {
        super("blockStack", 0xFF663300);
    }
    
    @Override
    public void init() {
        try {
            filterBlock = ItemHelper.getBlock(stack);
            filterMeta = filterBlock.getMetaFromState(filterBlock.getStateFromMeta((stack).getItemDamage()));
        } catch (Exception e) {}
    }

    @Override
    public boolean shouldReflectionSkipField(String name) {
        return name.equals("stack");
    }

    @Override
    public void addSpecialFields(List<IField> fields, DisplayMode mode) {
        if (mode == DisplayMode.EDIT) {
            fields.add(new ItemField("stack", this, 30, 35, 2.4F, Type.TRIGGER, BlockFilter.INSTANCE));
        }
    }

    @Override
    protected boolean matches(Block block, int meta) {
        if (block != filterBlock) return false;
        if (matchState && (meta != filterMeta)) return false;
        return true;
    }
}
