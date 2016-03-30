package joshie.progression.criteria.rewards;

import joshie.progression.api.criteria.IProgressionField;
import joshie.progression.api.criteria.IProgressionFilter;
import joshie.progression.api.special.DisplayMode;
import joshie.progression.api.special.ISpecialFieldProvider;
import joshie.progression.gui.fields.ItemFilterFieldPreview;
import joshie.progression.items.ItemCriteria;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import java.util.List;

public class RewardClear extends RewardBaseItemFilter implements ISpecialFieldProvider {
    public int toTake = 1;

    public RewardClear() {
        super(ItemCriteria.getStackFromMeta(ItemCriteria.ItemMeta.clearInventory), "clear", 0xFF69008C);
    }

    @Override
    public void addSpecialFields(List<IProgressionField> fields, DisplayMode mode) {
        if (mode == DisplayMode.EDIT) fields.add(new ItemFilterFieldPreview("filters", this, 25, 30, 2.8F));
    }

    @Override
    public void reward(EntityPlayerMP player) {
        int taken = 0;
        for (int i = 0; i < player.inventory.mainInventory.length && taken < toTake; i++) {
            ItemStack check = player.inventory.mainInventory[i];
            int decrease = 0;

            if (check != null) {
                for (int j = 0; j < check.stackSize && taken < toTake; j++) {
                    for (IProgressionFilter filter : filters) {
                        if (filter.matches(check)) {
                            decrease++;
                            taken++;
                        }
                    }
                }

                if (decrease > 0) player.inventory.decrStackSize(i, decrease);
            }
        }
    }

    @Override
    public ItemStack getIcon() {
        return BROKEN;
    }

    @Override
    public void addTooltip(List list) {
        list.add(EnumChatFormatting.WHITE + "Remove Item");
        ItemStack stack = preview == null ? BROKEN : preview;
        list.add(stack.getDisplayName() + " x" + stack.stackSize);
    }
}
