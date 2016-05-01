package joshie.progression.criteria.conditions;

import joshie.progression.Progression;
import joshie.progression.api.IPlayerTeam;
import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.criteria.IField;
import joshie.progression.api.criteria.IFilterProvider;
import joshie.progression.api.criteria.ProgressionRule;
import joshie.progression.api.special.*;
import joshie.progression.gui.fields.ItemFilterFieldPreview;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.List;

@ProgressionRule(name="ininventory", color=0xFF660000)
public class ConditionInInventory extends ConditionBaseItemFilter implements ICustomDescription, IEnum, ISpecialFieldProvider, IStackSizeable {
    private static enum CheckSlots {
        HELD(true, false), ARMOR, HOTBAR, INVENTORY, OFFHAND(false, true), ANYHAND(true, true);

        public boolean main;
        public boolean off;

        private CheckSlots() {}
        private CheckSlots(boolean main, boolean off) {
            this.main = main;
            this.off = off;
        }
    }

    public int stackSize = 1;
    public CheckSlots slotType = CheckSlots.INVENTORY;

    @Override
    public String getDescription() {
        if (getProvider().isInverted()) return Progression.format(getProvider().getUnlocalisedName() + ".description." + slotType.toString().toLowerCase() + ".inverted");
        else return Progression.format(getProvider().getUnlocalisedName() + ".description." + slotType.toString().toLowerCase());
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
    public boolean isEnum(String name) {
        return name.equals("slotType");
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
    public boolean isSatisfied(IPlayerTeam team) {
        int counter = 0;
        for (EntityPlayer player: team.getTeamEntities()) {
            if (!team.isTrueTeam()) counter = 0; //Reset the counter
            if (slotType == CheckSlots.ARMOR) {
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
            } else {
                if (slotType.main && matches(player.getHeldItemMainhand())) {
                    counter += player.getHeldItemMainhand().stackSize;
                    if (counter >= stackSize) return true;
                }

                if (slotType.off && matches(player.getHeldItemOffhand())) {
                    counter += player.getHeldItemOffhand().stackSize;
                    if (counter >= stackSize) return true;
                }
            }
        }

        return false;
    }

    //Helper Methods
    private boolean matches(ItemStack check) {
        for (IFilterProvider filter : filters) {
            if (filter.getProvided().matches(check)) return true;
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
}
