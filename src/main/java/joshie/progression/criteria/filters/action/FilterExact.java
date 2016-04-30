package joshie.progression.criteria.filters.action;

import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.criteria.IField;
import joshie.progression.api.criteria.ProgressionRule;
import joshie.progression.api.special.DisplayMode;
import joshie.progression.api.special.ICustomDescription;
import joshie.progression.api.special.ICustomWidth;
import joshie.progression.api.special.ISpecialFieldProvider;
import joshie.progression.crafting.ActionType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import java.util.List;

@ProgressionRule(name="exact", color=0xFF663300)
public class FilterExact extends FilterBaseAction implements ICustomWidth, ICustomDescription, ISpecialFieldProvider {
    public ItemStack stack = new ItemStack(Blocks.CRAFTING_TABLE);

    @Override
    public int getWidth(DisplayMode mode) {
        return 55;
    }

    @Override
    public String getDescription() {
        return ActionType.getCraftingActionFromIcon(stack).getDisplayName();
    }

    @Override
    public void addSpecialFields(List<IField> fields, DisplayMode mode) {
        if (mode == DisplayMode.EDIT) fields.add(ProgressionAPI.fields.getItem(this, "stack", 5, 25, 2.8F));
    }
    
    @Override
    public ItemStack getRandom(EntityPlayer player) {
        return stack;
    }

    @Override
    public boolean matches(ItemStack check) {
        if (stack.getItem() != check.getItem()) return false;
        if ((stack.getItemDamage() != check.getItemDamage())) return false;
        return true;
    }
}
