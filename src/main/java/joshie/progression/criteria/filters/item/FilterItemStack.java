package joshie.progression.criteria.filters.item;

import joshie.progression.Progression;
import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.criteria.IProgressionField;
import joshie.progression.api.special.ISpecialFieldProvider;
import net.minecraft.item.ItemStack;

import java.util.List;

public class FilterItemStack extends FilterBaseItem implements ISpecialFieldProvider {
    public ItemStack stack = new ItemStack(Progression.item);
    public boolean matchDamage = true;
    public boolean matchNBT = false;

    public FilterItemStack() {
        super("itemStack", 0xFF663300);
    }
    
    @Override
    public void addSpecialFields(List<IProgressionField> fields, DisplayMode mode) {
        if (mode == DisplayMode.EDIT)
        fields.add(ProgressionAPI.fields.getItem(this, "stack", 30, 35, 1.4F));
    }

    @Override
    public boolean matches(ItemStack check) {
        if (stack.getItem() != check.getItem()) return false;
        if (matchDamage && (stack.getItemDamage() != check.getItemDamage())) return false;
        if (matchNBT && (!stack.getTagCompound().equals(check.getTagCompound()))) return false;
        return true;
    }
}
