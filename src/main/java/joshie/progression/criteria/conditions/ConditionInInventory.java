package joshie.progression.criteria.conditions;

import java.util.UUID;

import joshie.progression.gui.editors.SelectItem.Type;
import joshie.progression.gui.fields.BooleanField;
import joshie.progression.gui.fields.EnumField;
import joshie.progression.gui.fields.IEnum;
import joshie.progression.gui.fields.ItemField;
import joshie.progression.gui.fields.TextField;
import joshie.progression.helpers.JSONHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.google.gson.JsonObject;

public class ConditionInInventory extends ConditionBase implements IEnum {
    private static enum CheckSlots {
        HELD, ARMOR, HOTBAR, INVENTORY;
    }

    public ItemStack stack = new ItemStack(Blocks.crafting_table);
    public boolean matchDamage = true;
    public boolean matchNBT = false;
    public int itemAmount = 1;
    public CheckSlots slotType = CheckSlots.INVENTORY;

    public ConditionInInventory() {
        super("ininventory", 0xFF660000);
        list.add(new BooleanField("matchDamage", this));
        list.add(new BooleanField("matchNBT", this));
        list.add(new TextField("itemAmount", this));
        list.add(new EnumField("slotType", this));
        list.add(new ItemField("stack", this, 25, 60, 3F, 27, 69, 62, 107, Type.TRIGGER));
    }

    @Override
    public Enum next() {
        int id = slotType.ordinal() + 1;
        if (id < CheckSlots.values().length) {
            return CheckSlots.values()[id];
        }

        return CheckSlots.values()[0];
    }

    private CheckSlots getTypeFromString(String name) {
        for (CheckSlots s : CheckSlots.values()) {
            if (s.name().equalsIgnoreCase(name)) {
                return s;
            }
        }

        return CheckSlots.INVENTORY;
    }

    @Override
    public void readFromJSON(JsonObject data) {
        stack = JSONHelper.getItemStack(data, "item", stack);
        matchDamage = JSONHelper.getBoolean(data, "matchDamage", matchDamage);
        matchNBT = JSONHelper.getBoolean(data, "matchNBT", matchNBT);
        itemAmount = JSONHelper.getInteger(data, "itemAmount", itemAmount);
        slotType = getTypeFromString(JSONHelper.getString(data, "checkType", slotType.name().toLowerCase()));
    }

    @Override
    public void writeToJSON(JsonObject data) {
        JSONHelper.setItemStack(data, "item", stack);
        JSONHelper.setBoolean(data, "matchDamage", matchDamage, true);
        JSONHelper.setBoolean(data, "matchNBT", matchNBT, false);
        JSONHelper.setInteger(data, "itemAmount", itemAmount, 1);
        JSONHelper.setString(data, "checkType", slotType.name().toLowerCase(), CheckSlots.INVENTORY.name().toLowerCase());
    }

    private boolean matches(ItemStack check) {
        if (check == null) return false;
        if (stack.getItem() == check.getItem()) {
            if (matchDamage) if (stack.getItemDamage() != check.getItemDamage()) return false;
            if (matchNBT) if (stack.getTagCompound() != check.getTagCompound()) return false;
            return true;
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
