package joshie.progression.criteria.filters.block;

import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.criteria.IProgressionField;
import joshie.progression.api.special.IInit;
import joshie.progression.api.special.ISpecialFieldProvider;
import joshie.progression.helpers.ItemHelper;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import java.util.List;

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
    public void addSpecialFields(List<IProgressionField> fields, DisplayMode mode) {
        if (mode == DisplayMode.EDIT) fields.add(ProgressionAPI.fields.getItem(this, "stack", 30, 35, 2.4F));
    }

    @Override
    protected boolean matches(Block block, int meta) {
        if (block != filterBlock) return false;
        if (matchState && (meta != filterMeta)) return false;
        return true;
    }
}
