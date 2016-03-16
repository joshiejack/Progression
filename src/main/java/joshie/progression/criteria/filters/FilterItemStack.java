package joshie.progression.criteria.filters;

import java.util.List;

import joshie.progression.api.IField;
import joshie.progression.api.gui.ISpecialFieldProvider;
import joshie.progression.gui.fields.ItemField;
import joshie.progression.gui.newversion.overlays.FeatureItemSelector.Type;
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
    public void addSpecialFields(List<IField> fields) {
        fields.add(new ItemField("stack", this, 30, 35, 1.4F, 77, 100, 43, 68, Type.TRIGGER));
    }

    @Override
    public boolean matches(ItemStack check) {
        if (stack.getItem() != check.getItem()) return false;
        if (matchDamage && (stack.getItemDamage() != check.getItemDamage())) return false;
        if (matchNBT && (!stack.getTagCompound().equals(check.getTagCompound()))) return false;
        return true;
    }
}
