package joshie.progression.criteria.filters.item;

import java.util.List;

import joshie.progression.PCommonProxy;
import joshie.progression.api.criteria.IProgressionField;
import joshie.progression.api.special.ISpecialFieldProvider;
import joshie.progression.gui.editors.FeatureItemSelector.Type;
import joshie.progression.gui.fields.ItemField;
import joshie.progression.gui.filters.FilterSelectorItem;
import net.minecraft.item.ItemStack;

public class FilterItemStack extends FilterBaseItem implements ISpecialFieldProvider {
    public ItemStack stack = new ItemStack(PCommonProxy.item);
    public boolean matchDamage = true;
    public boolean matchNBT = false;

    public FilterItemStack() {
        super("itemStack", 0xFF663300);
    }
    
    @Override
    public boolean shouldReflectionSkipField(String name) {
        return name.equals("stack");
    }

    @Override
    public void addSpecialFields(List<IProgressionField> fields, DisplayMode mode) {
        if (mode == DisplayMode.EDIT)
        fields.add(new ItemField("stack", this, 30, 35, 1.4F, Type.TRIGGER, FilterSelectorItem.INSTANCE));
    }

    @Override
    public boolean matches(ItemStack check) {
        if (stack.getItem() != check.getItem()) return false;
        if (matchDamage && (stack.getItemDamage() != check.getItemDamage())) return false;
        if (matchNBT && (!stack.getTagCompound().equals(check.getTagCompound()))) return false;
        return true;
    }
}
