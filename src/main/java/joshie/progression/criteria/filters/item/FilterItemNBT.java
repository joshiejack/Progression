package joshie.progression.criteria.filters.item;

import joshie.progression.api.fields.IInit;
import joshie.progression.helpers.StackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class FilterItemNBT extends FilterBaseItem implements IInit {
    public NBTTagCompound tagValue = new NBTTagCompound();
    public String tagText = "";

    public FilterItemNBT() {
        super("nbtString", 0xFF00B2B2);
    }
    
    @Override
    public void init() {
        tagValue = StackHelper.getTag(new String[] { tagText }, 0);
    }

    @Override
    public boolean matches(ItemStack check) { //TODO: Add Partial matching
        return check.hasTagCompound() && check.getTagCompound().equals(tagValue);
    }
}
