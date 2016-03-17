package joshie.progression.criteria.filters.block;

import java.util.List;

import joshie.progression.api.IField;
import joshie.progression.api.IInitAfterRead;
import joshie.progression.api.ISetterCallback;
import joshie.progression.api.gui.ISpecialFieldProvider;
import joshie.progression.gui.fields.ItemField;
import joshie.progression.gui.newversion.overlays.FeatureItemSelector.Type;
import joshie.progression.gui.selector.filters.BlockFilter;
import joshie.progression.helpers.ItemHelper;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class FilterBlockStack extends FilterBaseBlock implements ISpecialFieldProvider, ISetterCallback, IInitAfterRead {
    public ItemStack stack = new ItemStack(Blocks.anvil);
    public boolean matchState = true;
    private Block filterBlock = Blocks.anvil;
    private int filterMeta = 0;

    public FilterBlockStack() {
        super("blockStack", 0xFF663300);
    }

    @Override
    public void addSpecialFields(List<IField> fields) {
        fields.add(new ItemField("stack", this, 30, 35, 2.4F, 77, 100, 43, 68, Type.TRIGGER, BlockFilter.INSTANCE));
    }

    @Override
    protected boolean matches(Block block, int meta) {
        if (block != filterBlock) return false;
        if (matchState && (meta != filterMeta)) return false;
        return true;
    }

    @Override
    public boolean setField(String fieldName, Object object) {
        if (fieldName.equals("stack")) {
            try {
                filterBlock = ItemHelper.getBlock((ItemStack) object);
                filterMeta = filterBlock.getMetaFromState(filterBlock.getStateFromMeta(((ItemStack) object).getItemDamage()));
                stack = (ItemStack) object;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    @Override
    public void init() {
        setField("stack", stack);
    }
}
