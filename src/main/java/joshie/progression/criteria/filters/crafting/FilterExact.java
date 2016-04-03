package joshie.progression.criteria.filters.crafting;

import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.criteria.IField;
import joshie.progression.api.special.DisplayMode;
import joshie.progression.api.special.ISpecialFieldProvider;
import joshie.progression.crafting.ActionType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import java.util.List;

public class FilterExact extends FilterBaseCrafting implements ISpecialFieldProvider {
    public ItemStack stack = new ItemStack(Blocks.crafting_table);

    public FilterExact() {
        super("exact", 0xFF663300);
    }
    
    @Override
    public ItemStack getRandom(EntityPlayer player) {
        return stack;
    }

    @Override
    public void addSpecialFields(List<IField> fields, DisplayMode mode) {
        if (mode == DisplayMode.EDIT) fields.add(ProgressionAPI.fields.getItem(this, "stack", 5, 25, 2.8F));
    }

    @Override
    public String getDescription() {
        return ActionType.getCraftingActionFromIcon(stack).getDisplayName();
    }

    @Override
    public int getWidth(DisplayMode mode) {
        return 55;
    }

    @Override
    public boolean matches(ItemStack check) {
        if (stack.getItem() != check.getItem()) return false;
        if ((stack.getItemDamage() != check.getItemDamage())) return false;
        return true;
    }
}
