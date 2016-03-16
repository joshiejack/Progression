package joshie.progression.criteria.conditions;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import joshie.progression.api.IEnum;
import joshie.progression.api.IField;
import joshie.progression.api.IItemFilter;
import joshie.progression.api.gui.ISpecialFieldProvider;
import joshie.progression.gui.fields.ItemField;
import joshie.progression.gui.newversion.overlays.FeatureItemSelector.Type;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ConditionInInventory extends ConditionBase implements IEnum, ISpecialFieldProvider {
    private static enum CheckSlots {
        HELD, ARMOR, HOTBAR, INVENTORY;
    }

    public List<IItemFilter> filters = new ArrayList();
    public int itemAmount = 1;
    public CheckSlots slotType = CheckSlots.INVENTORY;

    public ConditionInInventory() {
        super("ininventory", 0xFF660000);
    }

    @Override
    public void addSpecialFields(List<IField> fields) {
        fields.add(new ItemField("stack", this, 25, 60, 3F, 27, 69, 62, 107, Type.TRIGGER));
    }

    @Override
    public Enum next() {
        int id = slotType.ordinal() + 1;
        if (id < CheckSlots.values().length) {
            return CheckSlots.values()[id];
        }

        return CheckSlots.values()[0];
    }

    private boolean matches(ItemStack check) {
        for (IItemFilter filter : filters) {
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
    public boolean isSatisfied(World world, EntityPlayer player, UUID uuid) {
        if (player == null) return false;
        if (slotType == CheckSlots.HELD) {
            if (matches(player.getCurrentEquippedItem())) return player.getCurrentEquippedItem().stackSize >= itemAmount;
        } else if (slotType == CheckSlots.ARMOR) {
            for (ItemStack armor : player.inventory.armorInventory) {
                if (armor != null) {
                    if (matches(armor)) return armor.stackSize >= itemAmount;
                }
            }

            return false;
        } else if (slotType == CheckSlots.HOTBAR) {
            return getAmount(player, 9) >= itemAmount;
        } else if (slotType == CheckSlots.INVENTORY) {
            return getAmount(player, 36) >= itemAmount;
        }

        return false;
    }
}
