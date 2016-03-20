package joshie.progression.criteria.filters.item;

import java.util.List;

import joshie.progression.api.fields.IField;
import joshie.progression.api.fields.ISpecialFieldProvider;
import joshie.progression.api.fields.ISpecialFieldProvider.DisplayMode;
import joshie.progression.gui.fields.ItemField;
import joshie.progression.gui.newversion.overlays.FeatureItemSelector.Type;
import joshie.progression.gui.selector.filters.ItemFilter;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class FilterItemStack extends FilterBaseItem implements ISpecialFieldProvider {
    public ItemStack stack = new ItemStack(Items.arrow);
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
    public void addSpecialFields(List<IField> fields, DisplayMode mode) {
        if (mode == DisplayMode.EDIT)
        fields.add(new ItemField("stack", this, 30, 35, 1.4F, Type.TRIGGER, ItemFilter.INSTANCE));
    }

    @Override
    public boolean matches(ItemStack check) {
        if (stack.getItem() != check.getItem()) return false;
        if (matchDamage && (stack.getItemDamage() != check.getItemDamage())) return false;
        if (matchNBT && (!stack.getTagCompound().equals(check.getTagCompound()))) return false;
        return true;
    }
}
