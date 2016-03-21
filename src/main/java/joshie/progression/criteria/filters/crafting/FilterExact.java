package joshie.progression.criteria.filters.crafting;

import java.util.List;

import joshie.progression.api.fields.IField;
import joshie.progression.api.fields.ISpecialFieldProvider;
import joshie.progression.gui.fields.ItemField;
import joshie.progression.gui.newversion.overlays.FeatureItemSelector.Type;
import joshie.progression.gui.selector.filters.ActionFilter;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import scala.actors.threadpool.Arrays;

public class FilterExact extends FilterBaseCrafting implements ISpecialFieldProvider {
    public ItemStack stack = new ItemStack(Blocks.crafting_table);

    public FilterExact() {
        super("exact", 0xFF663300);
    }
    
    @Override
    public List<ItemStack> getMatches(Object object) {
        return Arrays.asList(new Object[] { stack });
    }

    @Override
    public boolean shouldReflectionSkipField(String name) {
        return name.equals("stack");
    }

    @Override
    public void addSpecialFields(List<IField> fields, DisplayMode mode) {
        if (mode == DisplayMode.EDIT) fields.add(new ItemField("stack", this, 30, 35, 1.4F, Type.TRIGGER, ActionFilter.INSTANCE));
    }

    @Override
    public boolean matches(ItemStack check) {
        if (stack.getItem() != check.getItem()) return false;
        if ((stack.getItemDamage() != check.getItemDamage())) return false;
        return true;
    }
}
