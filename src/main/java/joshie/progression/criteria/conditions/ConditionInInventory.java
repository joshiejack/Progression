package joshie.progression.criteria.conditions;

import joshie.progression.Progression;
import joshie.progression.api.IPlayerTeam;
import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.criteria.IField;
import joshie.progression.api.criteria.IFilter;
import joshie.progression.api.special.DisplayMode;
import joshie.progression.api.special.IEnum;
import joshie.progression.api.special.ISpecialFieldProvider;
import joshie.progression.api.special.IStackSizeable;
import joshie.progression.gui.fields.ItemFilterFieldPreview;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.List;

public class ConditionInInventory extends ConditionBaseItemFilter implements IEnum, ISpecialFieldProvider, IStackSizeable {
    private static enum CheckSlots {
        HELD, ARMOR, HOTBAR, INVENTORY;
    }

    public int stackSize = 1;
    public CheckSlots slotType = CheckSlots.INVENTORY;

    public ConditionInInventory() {
        super("ininventory", 0xFF660000);
    }

    @Override
    public void addSpecialFields(List<IField> fields, DisplayMode mode) {
        if (mode == DisplayMode.EDIT) fields.add(ProgressionAPI.fields.getItemPreview(this, "filters", 30, 50, 1.9F));
        else fields.add(new ItemFilterFieldPreview("filters", this, 67, 40, 1.75F));
    }

    @Override
    public int getStackSize() {
        return stackSize;
    }

    @Override
    public Enum next(String name) {
        int id = slotType.ordinal() + 1;
        if (id < CheckSlots.values().length) {
            return CheckSlots.values()[id];
        }

        return CheckSlots.values()[0];
    }

    @Override
    public boolean isEnum(String name) {
        return name.equals("slotType");
    }

    private boolean matches(ItemStack check) {
        for (IFilter filter : filters) {
            if (filter.matches(check)) return true;
        }

        return false;
    }

    private int getAmount(EntityPlayer player, int slots) {
        boolean hasItem = false;
        for (int i = 0; i < slots; i++) {
            if (matches(player.inventory.mainInventory[i])) {
                hasItem = true;
                break;
            }
        }

        if (!hasItem) return 0;
        int amount = 0;
        for (int i = 0; i < slots; i++) {
            ItemStack stack = player.inventory.mainInventory[i];
            if (matches(stack)) {
                amount += stack.stackSize;
            }
        }

        return amount;
    }

    @Override
    public boolean isSatisfied(IPlayerTeam team) {
        int counter = 0;
        for (EntityPlayer player: team.getTeamEntities()) {
            if (!team.isTrueTeam()) counter = 0; //Reset the counter
            if (slotType == CheckSlots.HELD) {
                if (matches(player.getCurrentEquippedItem())) {
                    counter += player.getCurrentEquippedItem().stackSize;
                    if (counter >= stackSize) return true;
                }
            } else if (slotType == CheckSlots.ARMOR) {
                for (ItemStack armor : player.inventory.armorInventory) {
                    if (armor != null && matches(armor)) {
                        counter += armor.stackSize;
                        if (counter >= stackSize) return true;
                    }
                }
            } else if (slotType == CheckSlots.HOTBAR) {
                counter += getAmount(player, 9);
                if (counter >= stackSize) return true;
            } else if (slotType == CheckSlots.INVENTORY) {
                counter += getAmount(player, 36);
                if (counter >= stackSize) return true;
            }
        }

        return false;
    }

    @Override
    public int getWidth(DisplayMode mode) {
        return mode == DisplayMode.EDIT ? super.getWidth(mode) : 100;
    }

    @Override
    public String getConditionDescription() {
        if (inverted) return Progression.format(getUnlocalisedName() + ".description." + slotType.toString().toLowerCase() + ".inverted");
        else return Progression.format(getUnlocalisedName() + ".description." + slotType.toString().toLowerCase());
    }
}
