package joshie.progression.criteria.filters.item;

import joshie.progression.api.criteria.ProgressionRule;
import joshie.progression.api.special.IInit;
import joshie.progression.helpers.StackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

@ProgressionRule(name="nbtString", color=0xFF00B2B2)
public class FilterItemNBT extends FilterBaseItem implements IInit {
    public NBTTagCompound tagValue = new NBTTagCompound();
    public String tagText = "";

    @Override
    public void init(boolean isClient) {
        tagValue = StackHelper.getTag(new String[] { tagText }, 0);
    }

    @Override
    public boolean matches(ItemStack check) {
        return check.hasTagCompound() && check.getTagCompound().equals(tagValue);
    }
}
